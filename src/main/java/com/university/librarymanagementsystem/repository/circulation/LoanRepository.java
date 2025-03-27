package com.university.librarymanagementsystem.repository.circulation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.university.librarymanagementsystem.entity.circulation.Loan;
import com.university.librarymanagementsystem.enums.LoanStatus;

public interface LoanRepository extends JpaRepository<Loan, Integer> {
    List<Loan> findByDueDateBeforeAndReturnDateIsNullAndStatusNot(LocalDateTime dueDate, LoanStatus status);

    @Query(value = "SELECT * FROM loan WHERE status = 'OVERDUE'", nativeQuery = true)
    List<Loan> fetchAllOverdueLoans();

    @Query(value = "SELECT * FROM loan WHERE status != 'RETURNED'", nativeQuery = true)
    List<Loan> fetchAllUnreturnedLoans();

    @Query("SELECT l FROM Loan l WHERE l.account.account_id = :accountId AND l.dueDate = :dueDate")
    Optional<Loan> findByAccountIdAndDueDate(@Param("accountId") int accountId,
            @Param("dueDate") LocalDateTime dueDate);

}
