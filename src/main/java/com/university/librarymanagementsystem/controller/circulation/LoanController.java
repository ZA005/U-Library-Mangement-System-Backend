package com.university.librarymanagementsystem.controller.circulation;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.university.librarymanagementsystem.config.JWTAuthFilter;
import com.university.librarymanagementsystem.dto.circulation.LoanDTO;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.service.circulation.LoanService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/adminuser/loan")
public class LoanController {

    private LoanService loanService;

    @PostMapping("/add")
    public ResponseEntity<?> newLoan(@RequestBody LoanDTO loanDTO) {
        try {
            LoanDTO loan = loanService.newLoan(loanDTO);
            return new ResponseEntity<>(loan, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoan() {
        List<LoanDTO> loans = loanService.getAllLoans();

        return ResponseEntity.ok(loans);
    }

    @GetMapping("/unreturned")
    public ResponseEntity<List<LoanDTO>> getAllUnreturnedLoan() {
        List<LoanDTO> loans = loanService.getAllUnreturnLoans();

        return ResponseEntity.ok(loans);
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnLoanItem(@RequestBody LoanDTO loanDTO) {
        try {
            LoanDTO updatedLoan = loanService.returnLoanItem(loanDTO);
            return new ResponseEntity<>(updatedLoan, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}