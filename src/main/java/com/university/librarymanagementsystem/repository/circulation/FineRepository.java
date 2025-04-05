package com.university.librarymanagementsystem.repository.circulation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.university.librarymanagementsystem.entity.circulation.Fine;
import com.university.librarymanagementsystem.entity.circulation.Loan;

public interface FineRepository extends JpaRepository<Fine, Integer> {
    Optional<Fine> findByLoan(Loan loan);

    @Query(value = "SELECT * FROM fine WHERE status = 0", nativeQuery = true)
    List<Fine> findAllNonPaidFine();
}
