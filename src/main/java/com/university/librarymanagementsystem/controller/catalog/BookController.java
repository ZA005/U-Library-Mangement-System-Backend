package com.university.librarymanagementsystem.controller.catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.BookCatalogDTO;
import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.mapper.catalog.BookMapper;
import com.university.librarymanagementsystem.service.catalog.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
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

}
