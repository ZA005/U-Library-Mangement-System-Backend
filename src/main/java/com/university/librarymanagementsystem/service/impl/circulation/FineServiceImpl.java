package com.university.librarymanagementsystem.service.impl.circulation;

import com.university.librarymanagementsystem.dto.circulation.FineDTO;
import com.university.librarymanagementsystem.entity.circulation.Fine;
import com.university.librarymanagementsystem.entity.circulation.Loan;
import com.university.librarymanagementsystem.entity.circulation.Overdue;
import com.university.librarymanagementsystem.entity.circulation.TransactionHistory;
import com.university.librarymanagementsystem.enums.TransactionType;
import com.university.librarymanagementsystem.mapper.circulation.FineMapper;
import com.university.librarymanagementsystem.repository.circulation.FineRepository;
import com.university.librarymanagementsystem.repository.circulation.LoanRepository;
import com.university.librarymanagementsystem.repository.circulation.OverdueRepository;
import com.university.librarymanagementsystem.repository.circulation.TransactionRepository;
import com.university.librarymanagementsystem.service.circulation.FineService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Implementation of the FineService interface responsible for calculating,
 * retrieving,
 * and updating fines for overdue library loans.
 */
@Service
@AllArgsConstructor
public class FineServiceImpl implements FineService {

    // Fine rates
    private static final BigDecimal HOURLY_FINE_RATE = new BigDecimal("1.00"); // Fine per hour
    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("24.00"); // Fine per day

    // Repository dependencies
    private LoanRepository loanRepo;
    private FineRepository fineRepo;
    private OverdueRepository overdueRepo;
    private TransactionRepository transactionRepo;

    @Override
    public void markFineAsPaid(int fineId) {
        Optional<Fine> fine = fineRepo.findById(fineId);

        if (fine.isPresent()) {
            Fine objectFine = fine.get();
            Loan loan = objectFine.getLoan();

            if (loan.getReturnDate() == null) {
                throw new IllegalStateException(
                        "You cannot mark this fine as paid because the book has not been returned yet. Please return the book first.");
            }

            LocalDateTime today = LocalDateTime.now();
            objectFine.setStatus((byte) 1);
            objectFine.setPaymentDate(today);

            fineRepo.save(objectFine);

            TransactionHistory transaction = new TransactionHistory();
            transaction.setTransactionType(TransactionType.FINE_PAYMENT);
            transaction.setFine(objectFine);
            transaction.setTransactionDate(today);

            transactionRepo.save(transaction);
        } else {
            throw new IllegalArgumentException("The fine record you are trying to update does not exist.");
        }
    }

    /**
     * Scheduled method that runs every hour to calculate fines for overdue loans.
     * It retrieves all overdue records, determines the fine amount, and either
     * updates
     * an existing fine or creates a new fine entry.
     */
    @Scheduled(fixedRate = 3600000) // Runs every hour (3600000ms)
    @Transactional
    @Override
    public void calculateFine() {
        // Retrieve all overdue records from the database
        List<Overdue> overdueLoans = overdueRepo.findAll();

        // Loop through each overdue record
        for (Overdue overdue : overdueLoans) {
            // Find the corresponding loan based on account ID and due date
            List<Loan> loans = loanRepo.findByAccountIdAndDueDate(
                    overdue.getAccount().getAccount_id(), overdue.getDueDate());

            if (loans.isEmpty()) {
                continue;
            }

            if (loans.size() > 1) {
                throw new IllegalStateException(
                        String.format("Multiple loans found for accountId: %d and dueDate: %s",
                                overdue.getAccount().getAccount_id(), overdue.getDueDate()));
            }

            Loan loan = loans.get(0); // Get the loan entity

            // Calculate total fine amount based on overdue duration
            BigDecimal totalFine = calculateFineAmount(overdue.getTotalHoursOverdue(), overdue.getTotalDaysOverdue());

            // Check if a fine entry already exists for this loan
            Optional<Fine> existingFine = fineRepo.findByLoan(loan);

            if (existingFine.isPresent()) {
                // Update existing fine record with the new fine amount
                Fine fine = existingFine.get();

                if (fine.getPaymentDate() != null) {
                    continue; // Skip this row as payment has been made
                }
                fine.setFine_amount(totalFine);
                fineRepo.save(fine);
            } else {
                // Create a new fine entry in the database
                Fine newFine = new Fine();
                newFine.setLoan(loan);
                newFine.setAccount(overdue.getAccount()); // Associate fine with the account
                newFine.setFine_amount(totalFine);
                newFine.setStatus((byte) 0); // Status 0 = Unpaid
                newFine.setFineDate(LocalDateTime.now()); // Timestamp for when the fine was issued

                fineRepo.save(newFine); // Save new fine entry
            }
        }
    }

    /**
     * Retrieves all fines from the database.
     * 
     * @return List of all fines
     * @throws UnsupportedOperationException since it's not yet implemented.
     */
    @Override
    public List<FineDTO> getAllFines() {
        List<Fine> fines = fineRepo.findAll();

        return fines.stream().map((fine) -> FineMapper.mapToFineDTO(fine)).collect(Collectors.toList());
    }

    @Override
    public List<FineDTO> getAllNonPaidFines() {
        List<Fine> fines = fineRepo.findAllNonPaidFine();

        return fines.stream().map((fine) -> FineMapper.mapToFineDTO(fine)).collect(Collectors.toList());
    }

    /**
     * Retrieves a fine by loan ID.
     * 
     * @param loanId The ID of the loan for which the fine is to be retrieved.
     * @return The Fine object associated with the loan.
     * @throws UnsupportedOperationException since it's not yet implemented.
     */
    @Override
    public Fine getFineByLoanId(int loanId) {
        throw new UnsupportedOperationException("Unimplemented method 'getFineByLoanId'");
    }

    /**
     * Calculates the total fine amount based on the number of overdue hours and
     * days.
     * 
     * @param totalHours The total number of hours overdue.
     * @param totalDays  The total number of days overdue.
     * @return The computed fine amount.
     */
    private BigDecimal calculateFineAmount(long totalHours, long totalDays) {
        // Ensure totalHours does not double-count full days
        long remainingHours = totalHours - (totalDays * 24);

        // Calculate the fine using daily and remaining hourly rates
        BigDecimal dailyFine = DAILY_FINE_RATE.multiply(BigDecimal.valueOf(totalDays));
        BigDecimal hourlyFine = HOURLY_FINE_RATE.multiply(BigDecimal.valueOf(remainingHours));

        return dailyFine.add(hourlyFine);
    }

    @Override
    public Double getFineByUserId(String userId) {
        return fineRepo.getTotalActiveFineByUserId(userId);
    }
}
