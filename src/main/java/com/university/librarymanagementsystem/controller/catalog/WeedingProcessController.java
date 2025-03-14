package com.university.librarymanagementsystem.controller.catalog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.dto.catalog.WeedingProcessDTO;
import com.university.librarymanagementsystem.enums.WeedStatus;
import com.university.librarymanagementsystem.service.catalog.BookService;
import com.university.librarymanagementsystem.service.catalog.WeedingProcessService;
import com.university.librarymanagementsystem.service.catalog.WeedingStatusService;

@RestController
@RequestMapping("/adminuser/book/weeding")
public class WeedingProcessController {

    private final WeedingProcessService weedingProcessService;
    private final WeedingStatusService weedingStatusService;
    private final BookService bookService;

    public WeedingProcessController(WeedingProcessService weedingProcessService,
            BookService bookService, WeedingStatusService weedingStatusService) {
        this.weedingProcessService = weedingProcessService;
        this.weedingStatusService = weedingStatusService;
        this.bookService = bookService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<String> triggerManualWeedding(@RequestParam String initiator) {
        try {
            if (initiator == null || initiator.isEmpty()) {
                return ResponseEntity.badRequest().body("Initiator cannot be empty");
            }
            weedingProcessService.manualWeedingFlagging(initiator);
            return ResponseEntity.ok("Weeding process initiated");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error initiating weeding process");

        }
    }

    @PostMapping("weed")
    public ResponseEntity<WeedInfoDTO> weedBook(@RequestBody WeedInfoDTO weedInfoDTO) {
        try {
            if (weedInfoDTO == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (weedInfoDTO.getWeedStatus().equals(WeedStatus.WEEDED) || weedInfoDTO.getWeedStatus()
                    .equals(WeedStatus.ARCHIVED)) {
                bookService.weedBook(weedInfoDTO);
                weedingStatusService.updateWeedingStatus(weedInfoDTO);

                return new ResponseEntity<>(weedInfoDTO, HttpStatus.OK);
            } else {
                weedingStatusService.updateWeedingStatus(weedInfoDTO);
                weedingProcessService.updateWeedingProcess(weedInfoDTO);

                return new ResponseEntity<>(weedInfoDTO, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("update-process")
    public ResponseEntity<WeedingProcessDTO> updateProcess(@RequestBody WeedInfoDTO weedInfoDTO) {
        try {
            if (weedInfoDTO == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(weedingProcessService.updateWeedingProcess(weedInfoDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}
