package com.university.librarymanagementsystem.controller.catalog;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> searchBooks(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "publisher", required = false) String publisher,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "lccn", required = false) String lccn) {
        try {
            if (keyword == null && title == null && author == null &&
                    publisher == null && isbn == null && lccn == null) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("success", "false");
                errorResponse.put("message", "At least one search parameter is required");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            // Build the query using the service
            String query = googleBookService.buildQuery(keyword, title, author, publisher, isbn, lccn);

            // Perform the search
            String response = googleBookService.searchBooks(query);

            // Check if the response is empty or null
            if (response == null || response.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("success", "false");
                errorResponse.put("message", "No books found for the given query");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            // Return the response with 200 OK status
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("message", "An error occurred while searching books");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
