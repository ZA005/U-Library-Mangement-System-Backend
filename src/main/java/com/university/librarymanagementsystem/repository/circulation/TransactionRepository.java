package com.university.librarymanagementsystem.repository.circulation;

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

}
