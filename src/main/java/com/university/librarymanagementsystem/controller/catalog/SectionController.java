package com.university.librarymanagementsystem.controller.catalog;

import com.university.librarymanagementsystem.config.JWTAuthFilter;
import com.university.librarymanagementsystem.dto.catalog.LocationDTO;
import com.university.librarymanagementsystem.dto.catalog.SectionDTO;
import com.university.librarymanagementsystem.entity.catalog.Section;
import com.university.librarymanagementsystem.exception.DuplicateEntryException;
import com.university.librarymanagementsystem.service.catalog.LocationService;
import com.university.librarymanagementsystem.service.catalog.SectionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/section")
public class SectionController {

    private final JWTAuthFilter JWTAuthFilter;

    private final SectionService sectionService;

    private final LocationService locationService;

    public SectionController(SectionService sectionService, JWTAuthFilter JWTAuthFilter,
            LocationService locationService) {
        this.sectionService = sectionService;
        this.JWTAuthFilter = JWTAuthFilter;
        this.locationService = locationService;
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<List<SectionDTO>> fetchAllSectionsByLocationId(@PathVariable Integer locationId) {
        try {
            List<SectionDTO> sections = sectionService.fetchAllSections(locationId);
            if (sections == null || sections.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(sections, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching sections by location ID: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createSection(@RequestBody SectionDTO sectionDTO) {
        try {
            if (sectionDTO == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            SectionDTO createdSection = sectionService.addSection(sectionDTO);
            return new ResponseEntity<>(createdSection, HttpStatus.CREATED);
        } catch (DuplicateEntryException e) {
            return ResponseEntity.badRequest().body("Duplicate Section: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error creating section: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteSection(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            if (id == null) {
                response.put("success", "false");
                response.put("message", "Invalid section ID");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            boolean deleted = sectionService.deleteSection(id);
            if (deleted) {
                response.put("success", "true");
                response.put("message", "Section deleted successfully!");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", "false");
                response.put("message", "Section with ID " + id + " not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", "Error deleting section: " + e.getMessage());
            System.err.println("Error deleting section: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{sectionId}/status")
    public ResponseEntity<Section> updateSectionStatus(@PathVariable int sectionId,
            @RequestParam boolean status) {

        try {
            Section updatedSection = sectionService.updateSectionStatus(sectionId, status);
            if (updatedSection == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedSection, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error updating section status: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
