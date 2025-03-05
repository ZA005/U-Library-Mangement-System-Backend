package com.university.librarymanagementsystem.service.impl.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.catalog.BookCatalogDTO;
import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.entity.catalog.BookCatalog;
import com.university.librarymanagementsystem.entity.catalog.book.Author;
import com.university.librarymanagementsystem.dto.catalog.AuthorDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.mapper.catalog.BookCatalogMapper;
import com.university.librarymanagementsystem.mapper.catalog.BookMapper;
import com.university.librarymanagementsystem.repository.catalog.AuthorRepository;
import com.university.librarymanagementsystem.repository.catalog.BookCatalogRepository;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.repository.catalog.ConditionRepository;
import com.university.librarymanagementsystem.repository.catalog.LocationRepository;
import com.university.librarymanagementsystem.repository.catalog.SectionRepository;
import com.university.librarymanagementsystem.service.catalog.BookService;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookCatalogRepository bookCatalogRepository;
    private final AuthorRepository authorRepository;
    private final ConditionRepository conditionRepository;
    private final LocationRepository locationRepository;
    private final SectionRepository sectionRepository;

    public BookServiceImpl(BookRepository bookRepository,
            BookCatalogRepository bookCatalogRepository,
            AuthorRepository authorRepository,
            ConditionRepository conditionRepository,
            LocationRepository locationRepository,
            SectionRepository sectionRepository) {
        this.bookRepository = bookRepository;
        this.bookCatalogRepository = bookCatalogRepository;
        this.authorRepository = authorRepository;
        this.conditionRepository = conditionRepository;
        this.locationRepository = locationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Books saveBook(BookDTO bookDTO, BookCatalogDTO bookCatalogDTO) {

        if (bookDTO == null || bookCatalogDTO == null) {
            throw new IllegalArgumentException("DTOs cannot be null");
        }

        Books book = BookMapper.toBook(bookDTO);
        List<Author> authors = new ArrayList<>();
        if (bookDTO.getAuthors() != null && !bookDTO.getAuthors().isEmpty()) {
            for (String authorName : bookDTO.getAuthors()) {
                // Check if author already exists, otherwise create a new one
                Author author = authorRepository.findByName(authorName)
                        .orElseGet(() -> {
                            Author newAuthor = new Author();
                            newAuthor.setName(authorName);
                            newAuthor.setBooks(new ArrayList<>()); // Initialize the books list
                            return authorRepository.save(newAuthor);
                        });
                // Bidirectional relationship: add the book to the author's books list
                author.getBooks().add(book);
                authors.add(author);
            }
        }
        book.setAuthors(authors);
        Books savedBook = bookRepository.save(book);

        // Map and save BookCatalog with proper entity references
        BookCatalog catalog = BookCatalogMapper.mapToBookCatalog(bookCatalogDTO, savedBook);
        catalog.setCondition(conditionRepository.findById(bookCatalogDTO.getConditionId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid condition ID: " + bookCatalogDTO.getConditionId())));
        catalog.setLocation(locationRepository.findById(bookCatalogDTO.getLocation())
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid location ID: " + bookCatalogDTO.getLocation())));
        catalog.setSection(sectionRepository.findById(bookCatalogDTO.getSection())
                .orElseThrow(
                        () -> new IllegalArgumentException("Invalid section ID: " + bookCatalogDTO.getSection())));
        bookCatalogRepository.save(catalog);

        return savedBook;
    }

    @Override
    public BookCatalog getSavedBookCatalog(Books book) {
        return bookCatalogRepository.findByBookId(book)
                .orElseThrow(() -> new IllegalStateException("Catalog not found for book ID: " + book.getId()));
    }
}
