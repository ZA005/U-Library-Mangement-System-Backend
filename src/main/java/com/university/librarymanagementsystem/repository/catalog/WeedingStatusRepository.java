package com.university.librarymanagementsystem.repository.catalog;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.catalog.WeedingStatus;

@Repository
public interface WeedingStatusRepository extends JpaRepository<WeedingStatus, Integer> {

    Optional<WeedingStatus> findByBookId(int id);
}
