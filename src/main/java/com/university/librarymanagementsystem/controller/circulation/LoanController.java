package com.university.librarymanagementsystem.controller.circulation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.circulation.BorrowerDetailsDto;
import com.university.librarymanagementsystem.dto.circulation.LoanDetailsDTO;
import com.university.librarymanagementsystem.dto.circulation.LoanDto;
import com.university.librarymanagementsystem.dto.circulation.UserCirculationDetailsDTO;
import com.university.librarymanagementsystem.entity.circulation.Loans;
import com.university.librarymanagementsystem.service.circulation.LoanService;

@RestController
public class LoanController {

    @Autowired
    private LoanService loanService;

    @GetMapping("/admin/all-loans")
    public ResponseEntity<List<LoanDetailsDTO>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoanDetails());
    }

    @GetMapping("/admin/borrowed-loans")
    public ResponseEntity<List<LoanDetailsDTO>> getBorrowedLoans() {
        return ResponseEntity.ok(loanService.getAllLoanWithBorrowedStatus());
    }

    @GetMapping("/adminuser/borrower-details/{idNumber}")
    public BorrowerDetailsDto getBorrowerDetails(@PathVariable("idNumber") String idNumber) {
        return loanService.getBorrowerDetails(idNumber);
    }

    @PostMapping("/adminuser/save-loan")
    public ResponseEntity<Loans> saveLoan(@RequestBody LoanDetailsDTO loanDetailsDto) {
        return ResponseEntity.ok(loanService.saveLoan(loanDetailsDto));
    }

    // @GetMapping("/admin/loans/accessionNo/{id}")
    // public ResponseEntity<LoanDetailsDTO> getLoanDetailById(@PathVariable("id")
    // Long param) {
    // return ResponseEntity.ok(loanService.getLoanDetails(param));
    // }

    @PutMapping("/admin/update/{loanId}")
    public ResponseEntity<LoanDto> updateLoan(@PathVariable Long loanId, @RequestBody LoanDto loanDto,
            @RequestParam String action) {
        LoanDto updatedLoan = loanService.updateLoanStatus(loanId, loanDto, action);
        return ResponseEntity.ok(updatedLoan);
    }

    // @GetMapping("/admin/check-book-loan-status/accessionNo/{accessionNo}")
    // public ResponseEntity<Map<String, Boolean>> checkBookLoanStatus(@PathVariable
    // String accessionNo) {
    // boolean isLoaned = loanService.isBookLoanedByAccessionNo(accessionNo);
    // Map<String, Boolean> response = new HashMap<>();
    // response.put("isLoaned", isLoaned);
    // return ResponseEntity.ok(response);
    // }

    @GetMapping("/admin/overdue")
    public List<Loans> getOverdueLoans() {
        return loanService.getOverdueLoans();
    }

    @GetMapping
    public ResponseEntity<UserCirculationDetailsDTO> getUserCirculationDetails(String uncIdNumber) {
        return ResponseEntity.ok(loanService.getUserCirculationDetails(uncIdNumber));
    }

}
