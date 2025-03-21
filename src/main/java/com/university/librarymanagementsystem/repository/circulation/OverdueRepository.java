package com.university.librarymanagementsystem.repository.circulation;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.librarymanagementsystem.entity.circulation.Overdue;
import com.university.librarymanagementsystem.entity.user.Account;

public interface OverdueRepository extends JpaRepository<Overdue, Integer> {
    Optional<Overdue> findByAccountAndDueDate(Account account, LocalDateTime dueDate);

}
