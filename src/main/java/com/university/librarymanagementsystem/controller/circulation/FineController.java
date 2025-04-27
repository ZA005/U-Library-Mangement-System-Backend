package com.university.librarymanagementsystem.controller.circulation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.circulation.FineDTO;
import com.university.librarymanagementsystem.service.circulation.FineService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
// @RequestMapping("/admin")
public class FineController {
    private FineService service;

    @GetMapping("/admin/fine/all")
    public ResponseEntity<List<FineDTO>> getAllFines() {
        List<FineDTO> fines = service.getAllFines();

        return ResponseEntity.ok(fines);
    }

    @GetMapping("/admin/fine/not-paid")
    public ResponseEntity<List<FineDTO>> getAllNonPaid() {
        List<FineDTO> fines = service.getAllNonPaidFines();

        return ResponseEntity.ok(fines);
    }

    @PutMapping("/admin/fine/paid/{fineId}")
    public ResponseEntity<?> markFineAsPaid(@PathVariable int fineId) {
        try {
            service.markFineAsPaid(fineId);
            return ResponseEntity.ok("The fine has been successfully marked as paid.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("public/fine/active/{userId}")
    public ResponseEntity<?> getActiveFineByUserId(@PathVariable String userId) {
        Double totalActiveFine = service.getFineByUserId(userId);

        if (totalActiveFine == null || totalActiveFine == 0) {
            return ResponseEntity.ok("No active fine found for this user.");
        }
        return ResponseEntity.ok(totalActiveFine);
    }
}
