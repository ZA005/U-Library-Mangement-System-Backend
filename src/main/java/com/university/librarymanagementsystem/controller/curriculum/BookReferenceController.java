package com.university.librarymanagementsystem.controller.curriculum;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.dto.curriculum.BookReferenceDTO;
import com.university.librarymanagementsystem.service.curriculum.BookReferenceService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/public/reference")
public class BookReferenceController {
    private BookReferenceService bookReferenceService;

    @PostMapping
    public ResponseEntity<Object> addBookReference(@RequestBody BookReferenceDTO bookRefDTO) {
        try {
            BookReferenceDTO savedBookReference = bookReferenceService.addBookReference(bookRefDTO);
            return new ResponseEntity<>(savedBookReference, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("An unexpected error occurred: " + e.getMessage()));
        }
    }

    @PostMapping("/multiple")
    public ResponseEntity<Object> addMultipleBookReferences(@RequestBody List<BookReferenceDTO> bookRefDTOs) {
        try {
            List<BookReferenceDTO> savedBookReferences = bookReferenceService.addMultipleBookRef(bookRefDTOs);
            return new ResponseEntity<>(savedBookReferences, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(
                            "An unexpected error occurred while adding multiple references: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<BookReferenceDTO>> getAllBookReference() {
        List<BookReferenceDTO> bookReferences = bookReferenceService.getAllBookReference();

        return ResponseEntity.ok(bookReferences);
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<List<BookReferenceDTO>> getAllBookReferenceBySubject(@PathVariable("id") Integer courseId) {
        List<BookReferenceDTO> bookReferences = bookReferenceService.getAllBookRefByCourse(courseId);

        return ResponseEntity.ok(bookReferences);
    }

    @GetMapping("/book")
    public ResponseEntity<List<BookDTO>> getAllUniqueBooks() {
        List<BookDTO> uniqueBooks = bookReferenceService.getAllUniqueBooks();

        return ResponseEntity.ok(uniqueBooks);
    }

    @GetMapping("/book/course/{id}")
    public ResponseEntity<List<BookDTO>> getBooksNotReferenceInCourse(@PathVariable("id") Integer courseId) {
        List<BookDTO> books = bookReferenceService.getAllBooksNotReferenced(courseId);

        return ResponseEntity.ok(books);
    }

    @DeleteMapping("/remove/{bookRefId}")
    public ResponseEntity<Void> removeBookReference(@PathVariable Integer bookRefId) {
        try {
            bookReferenceService.removeBookRef(bookRefId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/export")
    public ResponseEntity<List<Object[]>> exportBookReferences(
            @RequestParam("revisionNo") Integer revisionNo,
            @RequestParam("programId") Integer programId) {

        List<Object[]> exportedReferences = bookReferenceService.exportBookReferences(revisionNo, programId);
        return new ResponseEntity<>(exportedReferences, HttpStatus.OK);
    }

    static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
