package com.university.librarymanagementsystem.controller.circulation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.circulation.FineDto;
import com.university.librarymanagementsystem.entity.circulation.Fine;
import com.university.librarymanagementsystem.service.circulation.FineService;

@RestController
@RequestMapping("/adminuser/")
public class FIneController {

    @Autowired
    private FineService fineService;

    @PostMapping("calculate")
    public ResponseEntity<List<Fine>> calculateFines() {
        List<Fine> fines = fineService.calculateAndSaveFines();
        return ResponseEntity.ok(fines);
    }

    @GetMapping("get-all-fines")
    public ResponseEntity<List<FineDto>> getAllFines() {
        return ResponseEntity.ok(fineService.getAllFines());
    }

    @GetMapping("get-all-fine-details")
    public ResponseEntity<List<FineDto>> getAllFineDetails() {
        return ResponseEntity.ok(fineService.getAllFinesDetails());
    }

    @PutMapping("paid/{fineId}")
    public ResponseEntity<String> updateFineStatus(@PathVariable Long fineId) {
        fineService.updateFineStatus(fineId);
        return ResponseEntity.ok("Fine status updated successfully.");
    }
}
