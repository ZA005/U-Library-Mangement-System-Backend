package com.university.librarymanagementsystem.service.impl.circulation;

import com.university.librarymanagementsystem.dto.circulation.FineDTO;
import com.university.librarymanagementsystem.entity.circulation.Fine;
import com.university.librarymanagementsystem.entity.circulation.Loan;
import com.university.librarymanagementsystem.entity.circulation.Overdue;
import com.university.librarymanagementsystem.mapper.circulation.FineMapper;
import com.university.librarymanagementsystem.repository.circulation.FineRepository;
import com.university.librarymanagementsystem.repository.circulation.LoanRepository;
import com.university.librarymanagementsystem.repository.circulation.OverdueRepository;
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
            Optional<Loan> loanOpt = loanRepo.findByAccountIdAndDueDate(
                    overdue.getAccount().getAccount_id(), overdue.getDueDate());

            // If no matching loan is found, skip this iteration
            if (loanOpt.isEmpty()) {
                continue;
            }

            Loan loan = loanOpt.get(); // Get the loan entity

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
     * Marks a fine as paid by updating its status in the database.
     * 
     * @param fineId The ID of the fine to mark as paid.
     * @throws UnsupportedOperationException since it's not yet implemented.
     */
    @Override
    public void markFineAsPaid(int fineId) {
        throw new UnsupportedOperationException("Unimplemented method 'markFineAsPaid'");
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
}
