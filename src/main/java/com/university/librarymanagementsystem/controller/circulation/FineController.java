package com.university.librarymanagementsystem.controller.circulation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.circulation.FineDTO;
import com.university.librarymanagementsystem.service.circulation.FineService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/admin")
public class FineController {
    private FineService service;

    @GetMapping("/fine/all")
    public ResponseEntity<List<FineDTO>> getAllFines() {
        List<FineDTO> fines = service.getAllFines();

        return ResponseEntity.ok(fines);
    }

    @GetMapping("/fine/not-paid")
    public ResponseEntity<List<FineDTO>> getAllNonPaid() {
        List<FineDTO> fines = service.getAllNonPaidFines();

        return ResponseEntity.ok(fines);
    }
}
