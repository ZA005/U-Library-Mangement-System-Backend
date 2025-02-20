package com.university.librarymanagementsystem.service.impl.catalog;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.AccessionDTO;
import com.university.librarymanagementsystem.dto.catalog.BookDto;
import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.dto.circulation.BookLoanDetailsDTO;
import com.university.librarymanagementsystem.entity.catalog.Book;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.catalog.BookMapper;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.service.catalog.BookService;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<BookDto> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .filter(book -> !book.getStatus().equals("WEEDED") && !book.getStatus().equals("ARCHIVED"))
                .map(BookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDto> getBooksByAuthorName(String authorName) {
        List<Book> books = bookRepository.findBooksByAuthorName(authorName);
        return books.stream()
                .filter(book -> !book.getStatus().equals("WEEDED") && !book.getStatus().equals("ARCHIVED"))
                .map(BookMapper::toDto).toList();
    }

    @Override
    public String getLastAccessionNumber(String locationPrefix) {
        // Fetch books by location prefix
        List<Book> books = bookRepository.findBooksByAccessionNo(locationPrefix);

        if (books.isEmpty()) {
            // If no books exist with this location prefix, start from c.1
            return locationPrefix + "-000001 c.1";
        }

        // Extract the highest number for base accession (without c.x)
        int maxNumber = books.stream()
                .map(Book::getAccessionNo)
                .map(accessionNo -> {
                    // Extract the numeric part before 'c.x'
                    String[] parts = accessionNo.split(" c.");
                    String numericPart = parts[0].replace(locationPrefix + "-", "");
                    try {
                        return Integer.parseInt(numericPart);
                    } catch (NumberFormatException e) {
                        return 0; // Fallback for invalid formats
                    }
                })
                .max(Integer::compare)
                .orElse(0);

        // Base number is ready for the next book, increment by 1 for a new base
        // accession number
        String newAccessionNumberBase = String.format("%s-%06d", locationPrefix, maxNumber + 1);

        // Now, find the highest 'c.x' for the same base accession number
        long maxCopyNumber = books.stream()
                .map(Book::getAccessionNo)
                .filter(accessionNo -> accessionNo.startsWith(newAccessionNumberBase))
                .mapToInt(accessionNo -> Integer.parseInt(accessionNo.split(" c.")[1]))
                .max()
                .orElse(0);

        // Generate the next available copy number 'c.x'
        return newAccessionNumberBase + " c." + (maxCopyNumber + 1);
    }

    // Book borrowing
    @Override
    public BookLoanDetailsDTO getBookByAccessionNo(String accessionNo) {
        // Retrieve the book by accession number
        Book book = bookRepository.findByAccessionNo(accessionNo)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + accessionNo));

        // Check if the book status is ARCHIVED, WEEDED, or LOST
        if ("ARCHIVED".equalsIgnoreCase(book.getStatus()) ||
                "WEEDED".equalsIgnoreCase(book.getStatus()) ||
                "LOST".equalsIgnoreCase(book.getStatus())) {
            throw new ResourceNotFoundException("The book is not available for borrowing. Status: " + book.getStatus());
        }

        // Proceed with mapping to BookLoanDetails if the book is available for
        // borrowing
        return BookMapper.mapToBookLoanDetails(book);
    }

    @Override
    public String getLatestAccessionNo(String title, String isbn10, String isbn13, String locationPrefix) {
        // Check if the book with the provided details exists
        if (bookRepository.existsByTitleAndIsbn10AndIsbn13(title, isbn10, isbn13)) {
            // Fetch the book with the latest accession number for the provided details
            Optional<Book> latestBook = bookRepository.findTopByTitleAndIsbn10AndIsbn13OrderByAccessionNoDesc(
                    title, isbn10, isbn13);

            if (latestBook.isPresent()) {
                String baseAccessionNo = latestBook.get().getAccessionNo().split(" c.")[0];

                // Fetch all books with the same base accession number
                List<Book> booksWithSameBase = bookRepository.findBooksByAccessionNo(baseAccessionNo);

                // Find the highest copy number for the base accession number
                int maxCopyNumber = booksWithSameBase.stream()
                        .map(Book::getAccessionNo)
                        .mapToInt(accessionNo -> {
                            try {
                                return Integer.parseInt(accessionNo.split(" c.")[1]);
                            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                                return 0; // Handle invalid formats gracefully
                            }
                        })
                        .max()
                        .orElse(0);

                // Generate the next copy number
                return baseAccessionNo + " c." + (maxCopyNumber);
            }
        } else {
            // If the book doesn't exist, fetch the latest accession number for the prefix
            Optional<Book> latestBookByPrefix = bookRepository.findTopByAccessionNoStartingWithOrderByAccessionNoDesc(
                    locationPrefix);

            String baseAccessionNo;
            if (latestBookByPrefix.isPresent()) {
                // Extract the numeric part of the latest accession number
                String latestAccessionNo = latestBookByPrefix.get().getAccessionNo();
                String[] parts = latestAccessionNo.split(" c.");
                String numericPart = parts[0].replace(locationPrefix + "-", "");

                int maxNumber;
                try {
                    maxNumber = Integer.parseInt(numericPart);
                } catch (NumberFormatException e) {
                    maxNumber = 0; // Handle invalid numeric parts
                }

                // Increment the base number for the new accession
                baseAccessionNo = String.format("%s-%06d", locationPrefix, maxNumber + 1);
            } else {
                // If no books exist with this prefix, start from "c.1"
                baseAccessionNo = locationPrefix + "-000001";
            }

            // The first copy for the new base accession number
            return baseAccessionNo + " c.1";
        }

        // If no conditions match, return "NOTFOUND"
        return "NOTFOUND";
    }

    @Override
    public String fetchLastAccessionNumber() {
        Optional<String> accessionNumber = bookRepository.findLastAddedBookAccessionNumber();
        return accessionNumber.orElseThrow(() -> new ResourceNotFoundException("No books found in the database."));
    }

    @Override
    public void weedBook(WeedInfoDTO weedInfoDTO) {
        Optional<Book> bookOptional = bookRepository.findById(weedInfoDTO.getBookId());
        if (bookOptional.isEmpty()) {
            throw new IllegalArgumentException("Book not found with ID: " + weedInfoDTO.getBookId());
        }

        Book book = bookOptional
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + weedInfoDTO.getBookId()));
        book.setStatus(weedInfoDTO.getWeedStatus().toString());
        bookRepository.save(book);
    }

    @Override
    public List<AccessionDTO> getAllAccessionNumbers() {
        List<Book> books = bookRepository.findAllAccessionsWithSections();
        return books.stream()
                .map(BookMapper::mapToAccessionDTO)
                .toList();
    }

    @Override
    public List<BookDto> getBooksByIsbn13(String isbn13) {
        List<Book> books = bookRepository.findBooksByIsbn13(isbn13);
        return books.stream()
                .filter(book -> !book.getStatus().equals("WEEDED") && !book.getStatus().equals("ARCHIVED"))
                .map(BookMapper::toDto).toList();
    }

}
