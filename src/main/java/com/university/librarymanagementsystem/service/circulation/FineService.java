package com.university.librarymanagementsystem.service.circulation;

import java.util.List;

import com.university.librarymanagementsystem.entity.circulation.Fine;
import com.university.librarymanagementsystem.entity.circulation.Loan;

public interface FineService {
    void calculateFine(Loan loan);

    List<Fine> getAllFines();

    Fine getFineByLoanId(int loanId);

    void markFineAsPaid(int fineId);
}
