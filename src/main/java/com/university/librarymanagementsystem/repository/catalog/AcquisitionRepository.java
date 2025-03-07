package com.university.librarymanagementsystem.repository.catalog;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.catalog.Acquisition;

import jakarta.transaction.Transactional;

@Repository
public interface AcquisitionRepository extends JpaRepository<Acquisition, Integer> {

    // this query is for fetching all records that status 0 or havent caloged yet
    // @Query(value = "SELECT * FROM acquisition WHERE status = 0", nativeQuery =
    // true)
    // List<Acquisition> getPendingRecords();

    @Query(value = "SELECT * FROM acquisition WHERE status = 0", countQuery = "SELECT COUNT(*) FROM acquisition WHERE status = 0", nativeQuery = true)
    Page<Acquisition> getPendingRecords(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE acquisition SET status = 1 WHERE id = :id", nativeQuery = true)
    int updatePendingStatus(@Param("id") Integer id);

}
