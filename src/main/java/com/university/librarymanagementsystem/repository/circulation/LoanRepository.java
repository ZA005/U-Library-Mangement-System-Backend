package com.university.librarymanagementsystem.repository.circulation;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.librarymanagementsystem.entity.circulation.Loan;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

}
