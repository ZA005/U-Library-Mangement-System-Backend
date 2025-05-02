package com.university.librarymanagementsystem.controller.catalog;

import com.university.librarymanagementsystem.dto.catalog.LocationDTO;
import com.university.librarymanagementsystem.entity.catalog.Location;
import com.university.librarymanagementsystem.exception.DuplicateEntryException;
import com.university.librarymanagementsystem.service.catalog.LocationService;

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
@RequestMapping("/adminuser/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;

    }

    @GetMapping
    public ResponseEntity<List<LocationDTO>> fetchAllLocations() {
        try {
            List<LocationDTO> locations = locationService.fetchAllLibraryLocations();
            if (locations == null || locations.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(locations, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error fetching all locations: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping
    public ResponseEntity<Object> createLocation(@RequestBody LocationDTO locationDTO) {
        try {
            if (locationDTO == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            LocationDTO createdLocation = locationService.addLocation(locationDTO);
            return new ResponseEntity<>(createdLocation, HttpStatus.CREATED);
        } catch (DuplicateEntryException e) {
            return ResponseEntity.badRequest().body("Duplicate Location: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error creating location: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteLocation(@PathVariable Integer id) {
        Map<String, String> response = new HashMap<>();
        try {
            if (id == null) {
                response.put("success", "false");
                response.put("message", "Invalid location ID");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            boolean deleted = locationService.deleteLocation(id);
            if (deleted) {
                response.put("success", "true");
                response.put("message", "Location deleted successfully!");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", "false");
                response.put("message", "Location with ID " + id + " not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", "Error deleting location: " + e.getMessage());
            System.err.println("Error deleting location: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Location> updateLocationStatus(@PathVariable int id, @RequestParam boolean status) {
        try {
            Location updatedLocation = locationService.updateLocationStatus(id, status);
            if (updatedLocation == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedLocation, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error updating location status: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
