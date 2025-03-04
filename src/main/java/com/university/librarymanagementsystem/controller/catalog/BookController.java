package com.university.librarymanagementsystem.controller.catalog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.BookRequestDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.mapper.catalog.BookCatalogMapper;
import com.university.librarymanagementsystem.mapper.catalog.BookMapper;
import com.university.librarymanagementsystem.service.catalog.BookService;

@RestController
public class BookController {

    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/admin/save")
    public ResponseEntity<BookRequestDTO> saveBook(@RequestBody BookRequestDTO requestDTO) {
        Books savedBook = bookService.saveBook(requestDTO.getBookDTO(), requestDTO.getBookCatalogDTO());

        // Create response DTO with both book and catalog info
        BookRequestDTO responseDTO = new BookRequestDTO(
                BookMapper.toBookDTO(savedBook),
                BookCatalogMapper.toBookCatalogDTO(bookService.getSavedBookCatalog(savedBook)));

        return ResponseEntity.ok(responseDTO);
    }
}
