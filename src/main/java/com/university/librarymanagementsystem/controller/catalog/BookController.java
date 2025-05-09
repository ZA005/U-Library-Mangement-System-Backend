package com.university.librarymanagementsystem.controller.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.BarcodeRequestDTO;
import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.dto.catalog.BookSearchRequestDTO;
import com.university.librarymanagementsystem.entity.catalog.BookCatalog;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.enums.BookStatus;
import com.university.librarymanagementsystem.mapper.catalog.BookMapper;
import com.university.librarymanagementsystem.repository.catalog.BookCatalogRepository;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.service.catalog.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class BookController {

    private BookService bookService;
    private BookRepository bookRepository;
    private BookCatalogRepository bookCatalogRepository;

    public BookController(BookService bookService, BookRepository bookRepository,
            BookCatalogRepository bookCatalogRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.bookCatalogRepository = bookCatalogRepository;
    }

    @PostMapping("/admin/book/save")
    public ResponseEntity<?> saveBook(@RequestBody BookDTO bookDTO) {
        try {
            // Validate input DTO
            if (bookDTO == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("success", "false");
                errorResponse.put("message", "BookCatalogDTO cannot be null");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
            List<Books> savedBooks = bookService.saveBook(bookDTO);

            List<BookDTO> responseDTOs = savedBooks.stream()
                    .map(BookMapper::mapToBookDTO)
                    .toList();
            return new ResponseEntity<>(responseDTOs, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("message", "Error saving book: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admin/book/fetchLatestBaseAccession")
    public ResponseEntity<String> fetchLatestBaseAccession(@RequestParam String isbn13,
            @RequestParam String locationCodeName) {
        try {
            String baseAccession = bookService.fetchLatestBaseAccession(isbn13, locationCodeName);
            if (baseAccession == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(baseAccession, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching Base Accession: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admin/book/fetchAllAccessionNumber")
    public ResponseEntity<List<BarcodeRequestDTO>> fetchAllAccessionNumberWithSection() {
        try {
            List<BarcodeRequestDTO> barcodeRequestDTOs = bookService.fetchAllAccessionNumberWithSection();
            if (barcodeRequestDTOs == null || barcodeRequestDTOs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(barcodeRequestDTOs, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching all Accession Number and Section: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/admin/book/generateCallNumber")
    public ResponseEntity<String> generateCallNumber(@RequestParam String title,
            @RequestParam String category, @RequestParam List<String> authors,
            @RequestParam String publishedDate) {
        try {
            if (title.isEmpty() && category.isBlank()
                    && authors.isEmpty() && publishedDate == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            String callNumber = bookService.generateCallNumber(
                    title,
                    category,
                    authors,
                    publishedDate);
            if ("Class number not found".equals(callNumber)) {
                return new ResponseEntity<>(callNumber, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(callNumber, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error generating Call Number: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/adminuser/book/fetchAll")
    public ResponseEntity<List<BookDTO>> fetchAllBooks() {
        try {
            List<BookDTO> books = bookService.fetchAllBooks();

            if (books == null || books.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if no books exist
            }

            // Return the list with 200 OK status
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching all books: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    @GetMapping("/public/book/fetchAllNewlyAcquired")
    public ResponseEntity<List<BookDTO>> fetchAllNewlyAcquiredBooks() {
        try {
            List<BookDTO> books = bookService.fetchNewlyAcquiredBooks();

            if (books == null || books.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found if no books exist
            }
            // Return the list with 200 OK status
            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching all books: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    @GetMapping("/adminuser/book/fetchByAuthor")
    public ResponseEntity<List<BookDTO>> fetchBooksByAuthor(@RequestParam String authorName) {
        try {
            if (authorName == null || authorName.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request if authorName is empty
            }

            return new ResponseEntity<>(bookService.fetchBooksByAuthor(authorName), HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching all books by authorName: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/adminuser/book/advanceSearch")
    public ResponseEntity<List<BookDTO>> advanceSearch(@RequestBody BookSearchRequestDTO request) {
        if (request == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            // Fetch books using the existing repository method
            List<Books> books = bookRepository.advanceSearchBooks(request);

            // Map of catalog ID to total copies from BookCatalog
            Map<Integer, Integer> totalCopiesMap = bookCatalogRepository.findAll()
                    .stream()
                    .collect(Collectors.toMap(
                            BookCatalog::getId,
                            BookCatalog::getCopies));

            // Map of catalog ID to number of LOANED_OUT books
            Map<Integer, Long> loanedOutCopiesMap = books.stream()
                    .filter(book -> BookStatus.LOANED_OUT.equals(book.getStatus()))
                    .collect(Collectors.groupingBy(
                            book -> book.getBookCatalog().getId(),
                            Collectors.counting()));

            // Filter out WEEDED and ARCHIVED books, deduplicate by isbn13, and map to DTO
            List<BookDTO> bookDTOs = books.stream()
                    .filter(book -> !book.getStatus().equals(BookStatus.WEEDED) &&
                            !book.getStatus().equals(BookStatus.ARCHIVED))
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(
                                    Books::getIsbn13, // Deduplicate by isbn13
                                    book -> {
                                        BookDTO dto = BookMapper.mapToBookDTO(book);
                                        int catalogId = book.getBookCatalog().getId();
                                        int totalCopies = totalCopiesMap.getOrDefault(catalogId, 0);
                                        long loanedOutCopies = loanedOutCopiesMap.getOrDefault(catalogId, 0L);
                                        int availableCopies = totalCopies - (int) loanedOutCopies;
                                        dto.getBookCatalog().setCopies(availableCopies < 0 ? 0 : availableCopies);
                                        return dto;
                                    },
                                    (dto1, dto2) -> dto1,
                                    LinkedHashMap::new),
                            map -> new ArrayList<>(map.values())));

            return new ResponseEntity<>(bookDTOs, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching books in advance search: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/adminuser/book/fetchByIsbn/{isbn13}")
    public ResponseEntity<List<BookDTO>> fetchBooksByISBN13(@PathVariable String isbn13) {
        if (isbn13 == null || isbn13.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(bookService.fetchBookByIsbn13(isbn13), HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("Error fetching books by isbn13: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/adminuser/book/{book_id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable int book_id) {
        try {
            // Fetch the book using the service method
            BookDTO bookDTO = bookService.fetchBookByID(book_id);

            // Return the DTO wrapped in a ResponseEntity with HTTP 200 status
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        } catch (Exception e) {
            // Return HTTP 404 if the book is not found or any other error occurs
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/adminuser/book/accession/{accession_number}")
    public ResponseEntity<BookDTO> getBookByAccessionNumber(@PathVariable String accession_number) {
        try {
            // Fetch the book using the service method
            BookDTO bookDTO = bookService.fetchBookByAccessionNumber(accession_number);

            // Return the DTO wrapped in a ResponseEntity with HTTP 200 status
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        } catch (Exception e) {
            // Return HTTP 404 if the book is not found or any other error occurs
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/adminuser/book/query/{query}")
    public ResponseEntity<List<BookDTO>> getByBookBy4Query(@PathVariable String query) {
        try {
            List<BookDTO> books = bookService.fetchBy4Query(query);

            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
