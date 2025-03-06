package com.university.librarymanagementsystem.service.impl.catalog;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.BookCatalogDTO;
import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.entity.catalog.BookCatalog;
import com.university.librarymanagementsystem.entity.catalog.Section;
import com.university.librarymanagementsystem.entity.catalog.book.Author;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.enums.BookStatus;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.catalog.BookCatalogMapper;
import com.university.librarymanagementsystem.mapper.catalog.BookMapper;
import com.university.librarymanagementsystem.repository.catalog.AuthorRepository;
import com.university.librarymanagementsystem.repository.catalog.BookCatalogRepository;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.repository.catalog.SectionRepository;
import com.university.librarymanagementsystem.service.catalog.BookService;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookCatalogRepository bookCatalogRepository;
    private final AuthorRepository authorRepository;
    private final SectionRepository sectionRepository;

    public BookServiceImpl(BookRepository bookRepository,
            BookCatalogRepository bookCatalogRepository,
            AuthorRepository authorRepository,
            SectionRepository sectionRepository) {
        this.bookRepository = bookRepository;
        this.bookCatalogRepository = bookCatalogRepository;
        this.authorRepository = authorRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public List<Books> saveBook(BookDTO bookDTO) {
        // Validate input DTO
        if (bookDTO == null) {
            throw new IllegalArgumentException("BookDTO cannot be null");
        }

        BookCatalogDTO bookCatalogDTO = bookDTO.getBookCatalog();
        if (bookCatalogDTO == null) {
            throw new IllegalArgumentException("BookCatalogDTO inside BookDTO cannot be null");
        }

        // Validate Section
        Section section = sectionRepository.findById(bookCatalogDTO.getSectionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Section not found with ID: " + bookCatalogDTO.getSectionId()));

        // Determine the number of copies
        Integer copies = bookCatalogDTO.getCopies() != null && bookCatalogDTO.getCopies() > 0
                ? bookCatalogDTO.getCopies()
                : 1;

        BookCatalog catalog = BookCatalogMapper.toBookCatalogEntity(bookCatalogDTO, section);

        catalog = bookCatalogRepository.save(catalog);

        String baseAccessionNumber = bookDTO.getAccessionNumber();
        List<Books> savedBooks = new ArrayList<>();
        for (int i = 1; i <= copies; i++) {
            // Generate accession number for each copy
            String accessionNumber = baseAccessionNumber + " c." + i;

            // Map BookDTO to Books entity
            Books book = BookMapper.mapToBook(bookDTO, catalog);
            // Handle authors
            List<Author> authors = new ArrayList<>();
            if (bookDTO.getAuthors() != null && !bookDTO.getAuthors().isEmpty()) {
                for (String authorName : bookDTO.getAuthors()) {
                    // Check if author already exists, otherwise create a new one
                    Author author = authorRepository.findByName(authorName)
                            .orElseGet(() -> {
                                Author newAuthor = new Author();
                                newAuthor.setName(authorName);
                                newAuthor.setBooks(new ArrayList<>());
                                return authorRepository.save(newAuthor);
                            });
                    // Bidirectional relationship: add the book to the author's books list
                    author.getBooks().add(book);
                    authors.add(author);
                }
            }
            // Set the authors on the book
            book.setAuthors(authors);
            book.setAccessionNumber(accessionNumber);

            // Save the Books entity
            Books savedBook = bookRepository.save(book);
            savedBooks.add(savedBook);
        }
        return savedBooks;
    }

    @Override
    public List<BookDTO> fetchAllBooks() {
        // Fetch all books from the repository
        List<Books> books = bookRepository.findAll();

        return books.stream()
                .filter(book -> !BookStatus.WEEDED.equals(book.getStatus())
                        && !BookStatus.ARCHIVED.equals(book.getStatus()))
                .map(BookMapper::mapToBookDTO)
                .toList();
    }

    @Override
    public List<BookCatalogDTO> fetchBooksByAuthor(String authorName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fetchBooksByAuthor'");
    }

}
