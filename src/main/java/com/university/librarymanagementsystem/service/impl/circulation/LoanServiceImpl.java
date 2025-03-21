package com.university.librarymanagementsystem.service.impl.circulation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.university.librarymanagementsystem.config.JWTAuthFilter;
import com.university.librarymanagementsystem.dto.circulation.LoanDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.entity.circulation.Loan;
import com.university.librarymanagementsystem.entity.circulation.Overdue;
import com.university.librarymanagementsystem.enums.BookStatus;
import com.university.librarymanagementsystem.enums.LoanStatus;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.circulation.LoanMapper;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.repository.circulation.LoanRepository;
import com.university.librarymanagementsystem.repository.circulation.OverdueRepository;
import com.university.librarymanagementsystem.repository.user.AccountRepository;
import com.university.librarymanagementsystem.service.circulation.LoanService;
import com.university.librarymanagementsystem.service.user.EmailService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {

    private BookRepository bookRepo;
    private AccountRepository accountRepo;
    private LoanRepository loanRepo;
    private OverdueRepository overdueRepo;
    private EmailService emailService;

    @Override
    public LoanDTO newLoan(LoanDTO loanDTO) {

        try {
            Books book = bookRepo.findByAccessionNumber(loanDTO.getBook_accession_no())
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

            if (BookStatus.LOANED_OUT.equals(book.getStatus())) {
                throw new IllegalStateException("Book is already loaned out");
            }

            Loan loan = LoanMapper.mapToLoan(loanDTO);
            Loan savedLoan = loanRepo.save(loan);

            book.setStatus(BookStatus.LOANED_OUT);
            bookRepo.save(book);

            emailService.sendEmail(loanDTO.getEmail(), "Borrowed", book.getTitle(), loanDTO.getDueDate().toString());
            return LoanMapper.mapToLoanDTO(savedLoan);
        } catch (Exception e) {
            throw new RuntimeException("Error processing loan request: " + e.getMessage(), e);
        }
    }

    @Override
    public List<LoanDTO> getAllLoans() {
        List<Loan> loans = loanRepo.findAll();

        return loans.stream().map((loan) -> LoanMapper.mapToLoanDTO(loan)).collect(Collectors.toList());
    }

    // AUTOMATICALLY UPDATE DATABASE WHEN A LOAN IS OVERDUE BY 1 SECOND
    @Scheduled(fixedRate = 60000)
    @Transactional
    @Override
    public void checkAndUpdateOverdueLoans() {
        LocalDateTime now = LocalDateTime.now();
        List<Loan> overdueLoans = loanRepo.findByDueDateBeforeAndReturnDateIsNullAndStatusNot(now,
                LoanStatus.OVERDUE);

        for (Loan loan : overdueLoans) {
            loan.setStatus(LoanStatus.OVERDUE);
            loanRepo.save(loan);
        }
    }

    // AUTOMATICALLY FETCH OVERDUE LOANS AND STORE IT TO OVERDUE TABLE
    @Scheduled(fixedRate = 60000)
    @Transactional
    @Override
    public void saveOverdueLoansToOverdueTable() {
        List<Loan> overdueLoans = loanRepo.fetchAllOverdueLoans();

        for (Loan loan : overdueLoans) {
            Overdue overdue = new Overdue();
            overdue.setAccount(loan.getAccount());
            overdue.setDueDate(loan.getDueDate());
            overdue.setReturnedDate(loan.getReturnDate());
            overdue.calculateOverdueDuration(); // Calculates overdue hours & days

            overdueRepo.save(overdue); // Save into overdue table
        }
    }
}