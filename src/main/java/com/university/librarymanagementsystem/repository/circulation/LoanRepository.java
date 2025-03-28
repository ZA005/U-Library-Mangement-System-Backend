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

    @Query(value = """
            SELECT
                CASE
                    WHEN u.role = 'LIBRARIAN' AND
                        (SELECT COUNT(*) FROM loan l WHERE l.account_id = a.account_id AND l.status = 'LOANED_OUT') >= 1
                    THEN 0
                    WHEN u.role = 'FACULTY' AND
                        (SELECT COUNT(*) FROM loan l WHERE l.account_id = a.account_id AND l.status = 'LOANED_OUT') >= 5
                    THEN 0
                    ELSE 1
                END
            FROM accounts a
            JOIN users u ON a.user_id = u.id
            WHERE a.account_id = :accountId
            """, nativeQuery = true)
    Integer canBorrowBook(@Param("accountId") int accountId);

}
