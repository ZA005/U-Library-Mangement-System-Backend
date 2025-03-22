package com.university.librarymanagementsystem.service.impl.circulation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.Duration;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.university.librarymanagementsystem.dto.circulation.LoanDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.entity.circulation.Loan;
import com.university.librarymanagementsystem.entity.circulation.Overdue;
import com.university.librarymanagementsystem.entity.circulation.Reservation;
import com.university.librarymanagementsystem.entity.circulation.TransactionHistory;
import com.university.librarymanagementsystem.entity.user.Account;
import com.university.librarymanagementsystem.enums.BookStatus;
import com.university.librarymanagementsystem.enums.LoanStatus;
import com.university.librarymanagementsystem.enums.ReservationStatus;
import com.university.librarymanagementsystem.enums.TransactionType;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.circulation.LoanMapper;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.repository.circulation.FineRepository;
import com.university.librarymanagementsystem.repository.circulation.LoanRepository;
import com.university.librarymanagementsystem.repository.circulation.OverdueRepository;
import com.university.librarymanagementsystem.repository.circulation.ReservationRepository;
import com.university.librarymanagementsystem.repository.circulation.TransactionRepository;
import com.university.librarymanagementsystem.repository.user.AccountRepository;
import com.university.librarymanagementsystem.service.circulation.LoanService;
import com.university.librarymanagementsystem.service.user.EmailService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {

    private BookRepository bookRepo;
    private LoanRepository loanRepo;
    private OverdueRepository overdueRepo;
    private ReservationRepository reservationRepo;
    private TransactionRepository transactionRepo;
    private EmailService emailService;

    @Override
    public LoanDTO newLoan(LoanDTO loanDTO) {
        try {
            Books book = bookRepo.findByAccessionNumber(loanDTO.getBook_accession_no())
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

            if (BookStatus.LOANED_OUT.equals(book.getStatus())) {
                throw new IllegalStateException("Book is already loaned out");
            }

            // Ensure loanDate is set
            LocalDateTime loanDate = loanDTO.getLoanDate() != null ? loanDTO.getLoanDate() : LocalDateTime.now();
            LocalDateTime dueDate = loanDate.plusDays(1);

            // Set dueDate in both DTO and entity
            loanDTO.setDueDate(dueDate);

            Loan loan = LoanMapper.mapToLoan(loanDTO);
            loan.setDueDate(dueDate);
            Loan savedLoan = loanRepo.save(loan);

            book.setStatus(BookStatus.LOANED_OUT);
            bookRepo.save(book);

            // Use null-safe handling to avoid null pointer exception
            String dueDateString = loanDTO.getDueDate() != null ? loanDTO.getDueDate().toString()
                    : "No due date assigned";
            emailService.sendEmail(loanDTO.getEmail(), "Borrowed", book.getTitle(), dueDateString);

            TransactionHistory transaction = new TransactionHistory();
            transaction.setTransactionType(TransactionType.LOAN);
            transaction.setLoan(savedLoan);
            transaction.setTransactionDate(LocalDateTime.now());

            transactionRepo.save(transaction);

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
            Optional<Overdue> existingOverdue = overdueRepo.findByAccountAndDueDate(loan.getAccount(),
                    loan.getDueDate());

            if (existingOverdue.isPresent()) {
                // If overdue entry exists, update return date and recalculate duration
                Overdue overdue = existingOverdue.get();
                overdue.setReturnedDate(loan.getReturnDate());
                overdue.calculateOverdueDuration();
                overdueRepo.save(overdue);
            } else {
                // If no existing entry, create a new Overdue record
                Overdue newOverdue = new Overdue();
                newOverdue.setAccount(loan.getAccount());
                newOverdue.setDueDate(loan.getDueDate());
                newOverdue.setReturnedDate(loan.getReturnDate());
                newOverdue.calculateOverdueDuration();

                overdueRepo.save(newOverdue);
            }

            // Create and store a new transaction for overdue loans
            TransactionHistory transaction = new TransactionHistory();
            transaction.setTransactionType(TransactionType.OVERDUE);
            transaction.setLoan(loan);
            transaction.setTransactionDate(LocalDateTime.now());

            transactionRepo.save(transaction);
        }
    }

    @Transactional
    @Override
    public LoanDTO returnLoanItem(LoanDTO loanDTO) {
        // Find the loan by ID
        Loan loan = loanRepo.findById(loanDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        LocalDateTime now = LocalDateTime.now();
        loan.setReturnDate(now);
        loan.setStatus(LoanStatus.RETURNED);
        loanRepo.save(loan);

        // Update Overdue entity if overdue
        Optional<Overdue> overdueOpt = overdueRepo.findByAccountAndDueDate(loan.getAccount(), loan.getDueDate());
        if (overdueOpt.isPresent()) {
            Overdue overdue = overdueOpt.get();
            overdue.setReturnedDate(now);

            // Recalculate overdue duration
            Duration overdueDuration = Duration.between(overdue.getDueDate(), now);
            overdue.setTotalHoursOverdue(overdueDuration.toHours());
            overdue.setTotalDaysOverdue(overdueDuration.toDays());

            overdueRepo.save(overdue);
        }

        // Update Book status
        Books book = loan.getBook();

        // Check if the book is reserved
        Optional<Reservation> nextReservation = reservationRepo.findFirstByBookAndStatusOrderByReservationDateAsc(
                book, ReservationStatus.PENDING);

        if (nextReservation.isPresent()) {
            // Notify the next borrower
            Reservation reservation = nextReservation.get();
            Account nextBorrower = reservation.getAccount();
            emailService.sendEmail(
                    nextBorrower.getUsers().getEmailAdd(),
                    "Notify",
                    book.getTitle(),
                    reservation.getExpirationDate().toString());

            // Update reservation status to NOTIFIED
            reservation.setStatus(ReservationStatus.NOTIFIED);
            reservationRepo.save(reservation);

            // Keep book status as RESERVED since it's reserved for the next borrower
            book.setStatus(BookStatus.RESERVED);
        } else {
            // No reservation, mark the book as AVAILABLE
            book.setStatus(BookStatus.AVAILABLE);
        }

        bookRepo.save(book);

        // Create and store a new transaction for returning the loan
        TransactionHistory transaction = new TransactionHistory();
        transaction.setTransactionType(TransactionType.RETURNED);
        transaction.setLoan(loan);
        transaction.setTransactionDate(now);
        transactionRepo.save(transaction);

        // Send email notification to the returning borrower
        emailService.sendEmail(loanDTO.getEmail(), "Returned",
                "You have successfully returned the book: " + book.getTitle(),
                loanDTO.getDueDate().toString());

        return LoanMapper.mapToLoanDTO(loan);
    }

    @Transactional
    @Override
    public LoanDTO renewLoanItem(LoanDTO loanDTO) {
        // Find the loan by ID
        Loan loan = loanRepo.findById(loanDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        // Extend the due date by 1 day
        LocalDateTime newDueDate = loan.getDueDate().plusDays(1);
        loan.setDueDate(newDueDate);
        loanRepo.save(loan);

        // Create a transaction record for renewal
        TransactionHistory transaction = new TransactionHistory();
        transaction.setTransactionType(TransactionType.RENEWED);
        transaction.setLoan(loan);
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepo.save(transaction);

        // Send email notification about the renewal
        emailService.sendEmail(loanDTO.getEmail(), "Renewed", loan.getBook().getTitle(), newDueDate.toString());

        return LoanMapper.mapToLoanDTO(loan);
    }
}