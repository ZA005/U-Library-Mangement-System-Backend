package com.university.librarymanagementsystem.repository.circulation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.university.librarymanagementsystem.entity.circulation.TransactionHistory;
import com.university.librarymanagementsystem.enums.TransactionType;

public interface TransactionRepository extends JpaRepository<TransactionHistory, Integer> {
        @Query("SELECT t FROM TransactionHistory t WHERE t.loan.id = :id AND t.transactionType = :transactionType")
        Optional<TransactionHistory> findByLoanAndTransactionType(@Param("id") Integer id,
                        @Param("transactionType") TransactionType transactionType);

        @Query(value = "SELECT t.* " +
                        "FROM transaction_history t " +
                        "LEFT JOIN loan l ON t.loan_id = l.id " +
                        "LEFT JOIN reservation r ON t.reservation_id = r.id " +
                        "LEFT JOIN fine f ON t.fine_id = f.id " +
                        "LEFT JOIN accounts a ON l.account_id = a.account_id OR r.account_id = a.account_id OR f.account_id = a.account_id "
                        +
                        "WHERE a.user_id = :userId " +
                        "ORDER BY t.id DESC", nativeQuery = true)
        List<TransactionHistory> findAllByUserId(@Param("userId") String userId);

        List<TransactionHistory> findAllByOrderByIdDesc();

}
