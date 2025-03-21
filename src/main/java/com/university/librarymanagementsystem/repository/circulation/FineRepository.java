package com.university.librarymanagementsystem.repository.circulation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.librarymanagementsystem.entity.circulation.Fine;
import com.university.librarymanagementsystem.entity.circulation.Loan;

public interface FineRepository extends JpaRepository<Fine, Integer> {
    Optional<Fine> findByLoan(Loan loan);
}
