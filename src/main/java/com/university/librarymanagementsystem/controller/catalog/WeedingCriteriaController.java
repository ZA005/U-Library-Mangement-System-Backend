package com.university.librarymanagementsystem.controller.catalog;

import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.WeedingCriteriaDTO;
import com.university.librarymanagementsystem.service.catalog.WeedingCriteriaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/adminuser/weeding-criteria")
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
    public ResponseEntity<HttpStatus> deleteWeedingCriteria(@PathVariable Integer id) {
        try {
            if (id == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            weedingCriteriaService.deleteCriteria(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error deleting the criteria: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
