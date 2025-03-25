package com.university.librarymanagementsystem.service.circulation;

import java.util.List;

import com.university.librarymanagementsystem.dto.circulation.FineDTO;
import com.university.librarymanagementsystem.entity.circulation.Fine;

public interface FineService {
    void calculateFine();

    List<FineDTO> getAllFines();

    Fine getFineByLoanId(int loanId);

    void markFineAsPaid(int fineId);
}
