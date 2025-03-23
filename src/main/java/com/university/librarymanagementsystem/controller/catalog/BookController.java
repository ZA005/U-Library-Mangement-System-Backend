package com.university.librarymanagementsystem.controller.catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.BarcodeRequestDTO;
import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.dto.catalog.BookSearchRequestDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.enums.BookStatus;
import com.university.librarymanagementsystem.mapper.catalog.BookMapper;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.service.catalog.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class BookController {

    private BookService bookService;
    private BookRepository bookRepository;

    public BookController(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
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

    @GetMapping("/adminuser/book/fetchAllNewlyAcquired")
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
            List<Books> books = bookRepository.advanceSearchBooks(request);
            List<BookDTO> bookDTO = books.stream().filter(book -> !book.getStatus().equals(BookStatus.WEEDED) &&
                    !book.getStatus().equals(BookStatus.ARCHIVED))
                    .map(BookMapper::mapToBookDTO).toList();
            return new ResponseEntity<>(bookDTO, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching all books by authorName: " + e.getMessage());
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

}
