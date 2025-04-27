package com.university.librarymanagementsystem.repository.circulation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.university.librarymanagementsystem.entity.circulation.Fine;
import com.university.librarymanagementsystem.entity.circulation.Loan;

public interface FineRepository extends JpaRepository<Fine, Integer> {
    Optional<Fine> findByLoan(Loan loan);

    @Query(value = "SELECT * FROM fine WHERE status = 0", nativeQuery = true)
    List<Fine> findAllNonPaidFine();

    @Query(value = "SELECT SUM(f.fine_amount) " +
            "FROM fine f " +
            "JOIN accounts a ON f.account_id = a.account_id " +
            "JOIN users u ON a.user_id = u.id " +
            "WHERE f.status = 0 " +
            "AND u.id = :userId", nativeQuery = true)
    Double getTotalActiveFineByUserId(@Param("userId") String userId);
}
