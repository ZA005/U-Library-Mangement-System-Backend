package com.university.librarymanagementsystem.repository.circulation;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.librarymanagementsystem.entity.circulation.Fine;

public interface FineRepository extends JpaRepository<Fine, Integer> {

}
