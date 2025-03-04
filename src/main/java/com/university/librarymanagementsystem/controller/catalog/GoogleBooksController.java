package com.university.librarymanagementsystem.controller.catalog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.service.catalog.GoogleBookService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/sru")
public class GoogleBooksController {

    private GoogleBookService googleBookService;

    @GetMapping("/googlebooks/search")
    public ResponseEntity<String> searchBooks(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "publisher", required = false) String publisher,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "lccn", required = false) String lccn) {

        String query = googleBookService.buildQuery(keyword, title, author,
                publisher, isbn, lccn);
        String response = googleBookService.searchBooks(query);
        return ResponseEntity.ok(response);
    }
}
