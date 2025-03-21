package com.university.librarymanagementsystem.service.impl.circulation;

import com.university.librarymanagementsystem.dto.circulation.FineDTO;
import com.university.librarymanagementsystem.entity.circulation.Fine;
import com.university.librarymanagementsystem.entity.circulation.Loan;
import com.university.librarymanagementsystem.mapper.circulation.FineMapper;
import com.university.librarymanagementsystem.repository.circulation.FineRepository;
import com.university.librarymanagementsystem.repository.circulation.LoanRepository;
import com.university.librarymanagementsystem.service.circulation.FineService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FineServiceImpl implements FineService {

    private static final BigDecimal HOURLY_FINE_RATE = new BigDecimal("1.00");
    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("24.00");

    private LoanRepository loanRepo;
    private FineRepository fineRepo;

    @Override
    @Transactional
    public void calculateFine(Loan loan) {
        if (loan.getReturnDate() == null || loan.getReturnDate().isBefore(loan.getDueDate())) {
            return;
        }

        // BigDecimal fineAmount =
        // DAILY_FINE_RATE.multiply(BigDecimal.valueOf(overdue.getTotalDaysOverdue()))
        // .add(HOURLY_FINE_RATE.multiply(BigDecimal.valueOf(overdue.getTotalHoursOverdue())));

        // Fine fine = FineMapper.mapToFine(new FineDTO(
        // 0,
        // loan.getId(),
        // loan.getAccount().getAccount_id(),
        // loan.getAccount().getUsername(),
        // fineAmount,
        // (byte) 0, // 0: Unpaid, 1: Paid
        // LocalDateTime.now(),
        // null));

        // fineRepo.save(fine);
    }

    @Override
    public List<Fine> getAllFines() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllFines'");
    }

    @Override
    public Fine getFineByLoanId(int loanId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFineByLoanId'");
    }

    @Override
    public void markFineAsPaid(int fineId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markFineAsPaid'");
    }

}
