package com.university.librarymanagementsystem.controller.catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.dto.catalog.SRUFormData;
import com.university.librarymanagementsystem.service.catalog.GoogleBookService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/sru")
public class GoogleBooksController {

    private GoogleBookService googleBookService;

    @PostMapping("/googlebooks/search")
    public ResponseEntity<?> searchBooks(@RequestBody SRUFormData sruFormData) {
        try {
            if (sruFormData == null ||
                    (sruFormData.getKeyword() == null && sruFormData.getTitle() == null &&
                            sruFormData.getAuthor() == null && sruFormData.getPublisher() == null &&
                            sruFormData.getIsbn() == null && sruFormData.getLccn() == null)) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("success", "false");
                errorResponse.put("message", "At least one search parameter is required");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            String query = googleBookService.buildQuery(
                    sruFormData.getKeyword(),
                    sruFormData.getTitle(),
                    sruFormData.getAuthor(),
                    sruFormData.getPublisher(),
                    sruFormData.getIsbn(),
                    sruFormData.getLccn());

            List<BookDTO> books = googleBookService.searchBooks(query);

            if (books.isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("success", "false");
                errorResponse.put("message", "No books found for the given query");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(books, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("message", "An error occurred while searching books");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
