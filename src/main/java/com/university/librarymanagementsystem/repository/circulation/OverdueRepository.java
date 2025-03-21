package com.university.librarymanagementsystem.repository.circulation;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.librarymanagementsystem.entity.circulation.Overdue;

public interface OverdueRepository extends JpaRepository<Overdue, Integer> {

}
