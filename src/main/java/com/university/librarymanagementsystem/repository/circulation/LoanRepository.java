package com.university.librarymanagementsystem.repository.circulation;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.circulation.Loans;

@Repository
public interface LoanRepository extends JpaRepository<Loans, Long> {

        List<Loans> findByStatus(String status);

        @Query(value = "SELECT * FROM loan l WHERE l.due_date < :currentDate", nativeQuery = true)
        List<Loans> findOverdueLoans(@Param("currentDate") LocalDateTime currentDate);

        List<Loans> findByBookAccessionNoAndStatus(String accessionNo, String status);

        @Query(value = "SELECT l FROM loan l WHERE l.return_date IS NULL AND l.due_date < :currentDate", nativeQuery = true)
        List<Loans> findOverdueLoansList(LocalDateTime currentDate);

        @Query("SELECT COUNT(l) FROM Loans l WHERE l.book.id = :bookId AND l.borrowDate >= :startDate")
        long countLoansByBookIdWithinLastYear(@Param("bookId") Long bookId,
                        @Param("startDate") LocalDateTime startDate);

}