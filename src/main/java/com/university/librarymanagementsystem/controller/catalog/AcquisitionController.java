package com.university.librarymanagementsystem.controller.catalog;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.AcquisitionDTO;
import com.university.librarymanagementsystem.service.catalog.AcquisitionService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/adminuser")
public class AcquisitionController {
    private AcquisitionService service;

    @PostMapping("/acquisition/add-record")
    public ResponseEntity<?> addRecords(@RequestBody List<AcquisitionDTO> acquisitionDTOs) {
        System.out.println("Received Acquisition Records:");
        acquisitionDTOs.forEach(record -> System.out.println(record));
        try {
            List<AcquisitionDTO> savedRecords = service.addRecords(acquisitionDTOs);
            return new ResponseEntity<>(savedRecords, HttpStatus.CREATED);
        } catch (IllegalStateException ex) {
            // Return conflict status with error message
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Duplicate record detected", "message", ex.getMessage()));
        } catch (Exception ex) {
            // Return generic server error with message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred", "message", ex.getMessage()));
        }
    }

    @GetMapping("/acquisition/pending")
    public ResponseEntity<List<AcquisitionDTO>> getPendingCatalogRecords() {
        List<AcquisitionDTO> pendingRecords = service.getPendingCatalogRecords();

        return ResponseEntity.ok(pendingRecords);
    }

    @PostMapping("/acquisition/update-status/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable Integer id) {
        boolean success = service.updateStatus(id);

        if (success) {
            return new ResponseEntity<>("Status updated successfully.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to update status.", HttpStatus.BAD_REQUEST);
        }
    }
}
