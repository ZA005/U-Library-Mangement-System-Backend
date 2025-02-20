package com.university.librarymanagementsystem.controller.catalog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.catalog.SectionDTO;
import com.university.librarymanagementsystem.service.catalog.SectionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/admin/section")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @GetMapping("/{locationId}")
    public ResponseEntity<List<SectionDTO>> getAllSectionsByLocationId(@PathVariable Integer locationId) {
        return ResponseEntity.ok(sectionService.getAllSections(locationId));
    }

    @PostMapping()
    public ResponseEntity<SectionDTO> createSection(@RequestBody SectionDTO sectionDTO) {
        return ResponseEntity.ok(sectionService.addSection(sectionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSection(@PathVariable Integer id) {
        sectionService.deleteSection(id);
        return ResponseEntity.ok().build();
    }
}
