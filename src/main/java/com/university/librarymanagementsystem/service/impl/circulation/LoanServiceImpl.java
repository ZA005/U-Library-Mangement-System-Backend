package com.university.librarymanagementsystem.service.impl.circulation;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.circulation.LoanDTO;
import com.university.librarymanagementsystem.entity.circulation.Loan;
import com.university.librarymanagementsystem.mapper.circulation.LoanMapper;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.repository.circulation.LoanRepository;
import com.university.librarymanagementsystem.repository.user.AccountRepository;
import com.university.librarymanagementsystem.service.circulation.LoanService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {
    private BookRepository bookRepo;
    private AccountRepository accountRepo;
    private LoanRepository loanRepo;

    @Override
    public LoanDTO newLoan(LoanDTO loanDTO) {
        Loan loan = LoanMapper.mapToLoan(loanDTO);
        return LoanMapper.mapToLoanDTO(loanRepo.save(loan));
    }

}