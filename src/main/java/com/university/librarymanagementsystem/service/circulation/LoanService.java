package com.university.librarymanagementsystem.service.circulation;

import java.util.List;

import com.university.librarymanagementsystem.dto.circulation.LoanDTO;

public interface LoanService {
    LoanDTO newLoan(LoanDTO loanDTO);

    List<LoanDTO> getAllLoans();

    void checkAndUpdateOverdueLoans();

    void saveOverdueLoansToOverdueTable();

    LoanDTO returnLoanItem(LoanDTO loanDTO);

    LoanDTO renewLoanItem(LoanDTO loanDTO);
}
