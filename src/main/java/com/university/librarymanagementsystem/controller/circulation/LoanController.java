package com.university.librarymanagementsystem.controller.circulation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.university.librarymanagementsystem.dto.circulation.LoanDTO;
import com.university.librarymanagementsystem.service.circulation.LoanService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/adminuser/loan")
public class LoanController {

    private LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanDTO> newLoan(@RequestParam LoanDTO loanDTO) {
        LoanDTO loan = loanService.newLoan(loanDTO);

        return new ResponseEntity<>(loan, HttpStatus.CREATED);
    }
}
