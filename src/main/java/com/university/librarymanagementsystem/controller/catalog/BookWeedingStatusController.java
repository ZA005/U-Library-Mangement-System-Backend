package com.university.librarymanagementsystem.controller.catalog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.service.catalog.BookWeedingStatusService;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/admin/book-weeding")
public class BookWeedingStatusController {

    @Autowired
    private BookWeedingStatusService bookWeedingStatusService;

    @GetMapping
    public ResponseEntity<List<WeedInfoDTO>> getAllBookWeedingStatus() {
        return ResponseEntity.ok(bookWeedingStatusService.getAllBookWeedingStatuses());
    }

}
