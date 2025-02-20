package com.university.librarymanagementsystem.service.impl.circulation;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.circulation.FineDto;
import com.university.librarymanagementsystem.entity.circulation.Fine;
import com.university.librarymanagementsystem.entity.circulation.Loans;
import com.university.librarymanagementsystem.mapper.circulation.FineMapper;
import com.university.librarymanagementsystem.repository.circulation.FineRepository;
import com.university.librarymanagementsystem.repository.circulation.LoanRepository;
import com.university.librarymanagementsystem.service.circulation.FineService;

@Service
public class FineServiceImpl implements FineService {

    private static final double PENALTY_RATE_PER_HOUR = 1.0; // One peso per hour for overdue books

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private FineRepository fineRepository;

    @Override
    public List<Fine> calculateAndSaveFines() {
        List<Loans> overdueLoans = loanRepository.findOverdueLoans(LocalDateTime.now());
        List<Fine> fines = new ArrayList<>();

        for (Loans loan : overdueLoans) {
            // Check if the fine already exists for this loan_id in the Fine table
            if (fineRepository.existsByLoan_LoanId(loan.getLoanId())) {
                continue; // Skip if the fine for this loan already exists
            }

            // Calculate the total overdue duration based on current time
            Duration duration = Duration.between(loan.getDueDate(), LocalDateTime.now());
            long hoursOverdue = duration.toHours(); // Total hours overdue

            double fineAmount = hoursOverdue * PENALTY_RATE_PER_HOUR; // Fine for overdue books

            // Check if the loan is an overnoon or overnight book and calculate the specific
            // fines
            if (isOvernoon(loan.getBorrowDate())) {
                fineAmount += calculateOvernoonFine();
            } else if (isOvernight(loan.getBorrowDate())) {
                fineAmount += calculateOvernightFine();
            }

            // Calculate additional fine for each day passed (if more than one day)
            long daysOverdue = duration.toDays();
            if (daysOverdue > 0) {
                fineAmount += daysOverdue * 24 * 1.0; // Extra fine for each day passed
            }

            // Create and save the fine
            Fine fine = new Fine();
            fine.setBorrowDate(loan.getBorrowDate());
            fine.setDueDate(loan.getDueDate());
            fine.setFineAmount(fineAmount);
            fine.setReturnDate(loan.getReturnDate()); // Set current time as return date for record
            fine.setLoan(loan);
            fine.setUser(loan.getUser());
            fine.setPaid(0);

            fines.add(fine);
        }

        return fineRepository.saveAll(fines); // Save only new fines
    }

    // Check if the borrowed book is considered overnoon (afternoon)
    private boolean isOvernoon(LocalDateTime borrowDate) {
        int hour = LocalDateTime.now().getHour(); // Use current time
        return hour >= 14 && hour < 18; // Between 2:00 PM and 6:00 PM
    }

    // Check if the borrowed book is considered overnight
    private boolean isOvernight(LocalDateTime borrowDate) {
        int hour = LocalDateTime.now().getHour(); // Use current time
        return hour >= 18 || hour < 8; // From 6:00 PM to 8:00 AM
    }

    // Calculate fine for overnoon books based on current time
    private double calculateOvernoonFine() {
        int hour = LocalDateTime.now().getHour(); // Use current time
        if (hour >= 14 && hour < 15)
            return 1.0;
        if (hour >= 15 && hour < 16)
            return 2.0;
        if (hour >= 16 && hour < 17)
            return 3.0;
        if (hour >= 17 && hour < 18)
            return 4.0;
        return 0;
    }

    // Calculate fine for overnight books based on current time
    private double calculateOvernightFine() {
        int hour = LocalDateTime.now().getHour(); // Use current time
        if (hour >= 20 && hour < 21)
            return 1.0;
        if (hour >= 21 && hour < 22)
            return 2.0;
        if (hour >= 22 && hour < 23)
            return 3.0;
        if (hour >= 23 && hour < 24)
            return 4.0;
        if (hour >= 0 && hour < 1)
            return 5.0;
        if (hour >= 1 && hour < 2)
            return 6.0;
        if (hour >= 2 && hour < 3)
            return 7.0;
        if (hour >= 3 && hour < 4)
            return 8.0;
        if (hour >= 4 && hour < 5)
            return 9.0;
        if (hour >= 5 && hour < 6)
            return 10.0;
        return 0;
    }

    @Override
    public List<FineDto> getAllFines() {
        List<Fine> fines = fineRepository.findAll();
        return fines.stream()
                .map(fine -> new FineDto(
                        fine.getFineId(),
                        fine.getLoan().getLoanId(),
                        fine.getUser().getUserId(),
                        null, null, null,
                        fine.getBorrowDate(),
                        fine.getDueDate(),
                        fine.getReturnDate(),
                        fine.getFineAmount(),
                        fine.getPaid()))
                .toList();
    }

    @Override
    public List<FineDto> getAllFinesDetails() {
        List<Object[]> result = fineRepository.findAllFineDetails();
        return result.stream().map(FineMapper::toFineDto).toList();
    }

    @Override
    public void updateFineStatus(Long fineId) {
        fineRepository.updateFineStatus(fineId);
    }
}
