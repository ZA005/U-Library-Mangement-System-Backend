package com.university.librarymanagementsystem.controller.catalog;

import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.WeedingCriteriaDTO;
import com.university.librarymanagementsystem.service.catalog.WeedingCriteriaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/admin/weeding-criteria")
public class WeedingCriteriaController {

    private final WeedingCriteriaService weedingCriteriaService;

    public WeedingCriteriaController(WeedingCriteriaService weedingCriteriaService) {
        this.weedingCriteriaService = weedingCriteriaService;
    }

    @PostMapping
    public ResponseEntity<WeedingCriteriaDTO> createCriteria(
            @RequestBody WeedingCriteriaDTO weedingCriteriaDTO) {
        try {
            if (weedingCriteriaDTO == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(weedingCriteriaService.createWeedingCriteria(
                    weedingCriteriaDTO), HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error saving the criteria: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<WeedingCriteriaDTO> updateWeedingCriteria(
            @RequestBody WeedingCriteriaDTO weedingCriteriaDTO) {
        try {
            if (weedingCriteriaDTO == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(weedingCriteriaService.updateWeedingCriteria(
                    weedingCriteriaDTO), HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error updating the criteria: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteWeedingCriteria(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();

        try {
            if (id == null) {
                response.put("success", "false");
                response.put("message", "Invalid criteria ID");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            boolean deleted = weedingCriteriaService.deleteCriteria(id);
            if (deleted) {
                response.put("success", "true");
                response.put("message", "Weeding criteria deleted successfully!");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", "false");
                response.put("message", "Weeding criteria with ID " + id + " not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", "Error deleting weeding criteria: " + e.getMessage());
            System.err.println("Error deleting the criteria: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<WeedingCriteriaDTO>> fetchAllWeedingCriteria() {
        try {
            List<WeedingCriteriaDTO> criteria = weedingCriteriaService.fetchAllWeedingCriteria();

            if (criteria == null || criteria.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(criteria, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching all the criteria: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeedingCriteriaDTO> fetchWeedingCriteriaById(@PathVariable Integer id) {
        try {
            if (id == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            WeedingCriteriaDTO weedingCriteriaDTO = weedingCriteriaService.fetchWeedingCriteriaById(id);
            if (weedingCriteriaDTO == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(weedingCriteriaDTO, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching the criteria with id : " + id + "error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
