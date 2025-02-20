package com.university.librarymanagementsystem.repository.circulation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.circulation.Fine;

import jakarta.transaction.Transactional;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {

    boolean existsByLoan_LoanId(Long loanId);

    @Query(value = "SELECT " +
            "f.fine_id AS fineId, " +
            "f.loan_id AS loanId, " +
            "f.user_id AS userId, " +
            "s.id AS stakeholderId, " +
            "s.first_name AS firstName, " +
            "s.last_name AS lastName, " +
            "f.borrow_date AS borrowDate, " +
            "f.due_date AS dueDate, " +
            "f.return_date AS returnDate, " +
            "f.fine_amount AS fineAmount, " +
            "f.paid AS paid " +
            "FROM fine f " +
            "JOIN users u ON f.user_id = u.user_id " +
            "JOIN stakeholders s ON u.school_id = s.id " +
            "WHERE f.paid = 0", nativeQuery = true)
    List<Object[]> findAllFineDetails();

    @Modifying
    @Transactional
    @Query(value = "UPDATE fine SET paid = 1 WHERE fine_id = :id", nativeQuery = true)
    void updateFineStatus(@Param("id") Long fineId);
}