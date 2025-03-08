package com.university.librarymanagementsystem.controller.catalog;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.WeedInfoDTO;
import com.university.librarymanagementsystem.service.catalog.WeedingStatusService;

@RestController
@RequestMapping("/adminuser/book/weed/status")
public class WeedingStatusController {

    private final WeedingStatusService weedStatusService;

    public WeedingStatusController(WeedingStatusService weedStatusService) {
        this.weedStatusService = weedStatusService;
    }

    @GetMapping
    public ResponseEntity<List<WeedInfoDTO>> fetchAllWeedingStatus() {
        try {
            List<WeedInfoDTO> weedInfos = weedStatusService.fetchAllWeedingStatus();
            if (weedInfos == null || weedInfos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(weedInfos, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching all the WeedingStatus: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
