package com.university.librarymanagementsystem.repository.catalog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.catalog.WeedingCriteria;

@Repository
public interface WeedingCriteriaRepository extends JpaRepository<WeedingCriteria, Integer> {

    List<WeedingCriteria> findAllByCriteriaStatusTrue();

}
