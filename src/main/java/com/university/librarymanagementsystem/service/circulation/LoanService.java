package com.university.librarymanagementsystem.service.circulation;

import com.university.librarymanagementsystem.dto.circulation.LoanDTO;

public interface LoanService {
    LoanDTO newLoan(LoanDTO loanDTO);
}
