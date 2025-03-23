package com.university.librarymanagementsystem.repository.circulation;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.librarymanagementsystem.entity.circulation.TransactionHistory;

public interface TransactionRepository extends JpaRepository<TransactionHistory, Integer> {

}
