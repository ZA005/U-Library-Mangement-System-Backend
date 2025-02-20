package com.university.librarymanagementsystem.controller.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.enums.WeedStatus;
import com.university.librarymanagementsystem.service.catalog.BookService;
import com.university.librarymanagementsystem.service.catalog.BookWeedingService;
import com.university.librarymanagementsystem.service.catalog.BookWeedingStatusService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/admin/book-weeding")
public class BookWeedingController {

    @Autowired
    private BookWeedingService bookWeedingService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookWeedingStatusService bookWeedingStatusService;

    @PostMapping("/test")
    public ResponseEntity<String> triggerManualWeeding() {
        bookWeedingService.manualWeedingFlagging();
        return ResponseEntity.ok("Manual weeding process has been started.");
    }

    @PostMapping("/weed")
    public ResponseEntity<WeedInfoDTO> weedBook(@RequestBody WeedInfoDTO weedInfoDTO) {

        if (weedInfoDTO.getWeedStatus().equals(WeedStatus.WEEDED)
                || weedInfoDTO.getWeedStatus().equals(WeedStatus.ARCHIVED)) {

            bookService.weedBook(weedInfoDTO);
            bookWeedingStatusService.updateBookWeedingStatus(weedInfoDTO);

            // Since the book has been weeded or archived, we return it after updating
            return ResponseEntity.ok(weedInfoDTO);
        } else {
            bookWeedingStatusService.updateBookWeedingStatus(weedInfoDTO);
            bookWeedingService.updateBookWeeding(weedInfoDTO);

            // Return the updated weedInfoDTO for any other status update
            return ResponseEntity.ok(weedInfoDTO);
        }
    }

    @PostMapping("/weed-process")
    public ResponseEntity<WeedInfoDTO> updateProcess(@RequestBody WeedInfoDTO weedInfoDTO) {
        bookWeedingService.updateBookWeeding(weedInfoDTO);
        return ResponseEntity.ok(weedInfoDTO);
    }

}
