package com.university.librarymanagementsystem.repository.circulation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.university.librarymanagementsystem.entity.circulation.Loan;
import com.university.librarymanagementsystem.enums.LoanStatus;

public interface LoanRepository extends JpaRepository<Loan, Integer> {
    List<Loan> findByDueDateBeforeAndReturnDateIsNullAndStatusNot(LocalDateTime dueDate, LoanStatus status);

    @Query(value = "SELECT * FROM loan WHERE status = 'OVERDUE'", nativeQuery = true)
    List<Loan> fetchAllOverdueLoans();
}
