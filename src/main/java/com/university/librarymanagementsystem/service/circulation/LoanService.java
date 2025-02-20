package com.university.librarymanagementsystem.service.circulation;

import java.util.List;

import com.university.librarymanagementsystem.dto.circulation.BorrowerDetailsDto;
import com.university.librarymanagementsystem.dto.circulation.LoanDetailsDTO;
import com.university.librarymanagementsystem.dto.circulation.LoanDto;
import com.university.librarymanagementsystem.dto.circulation.UserCirculationDetailsDTO;
import com.university.librarymanagementsystem.entity.circulation.Loans;

public interface LoanService {
    List<LoanDetailsDTO> getAllLoanDetails();

    List<LoanDetailsDTO> getAllLoanWithBorrowedStatus();

    BorrowerDetailsDto getBorrowerDetails(String libraryCardNumber);

    Loans saveLoan(LoanDetailsDTO loanDetailsDTO);

    LoanDetailsDTO getLoanDetails(Long loanId);

    LoanDto updateLoanStatus(Long loanId, LoanDto loanDto, String action);

    List<Loans> getOverdueLoans();

    boolean isBookLoanedByAccessionNo(String accessionNo);

    UserCirculationDetailsDTO getUserCirculationDetails(String uncIdNumber);

}
