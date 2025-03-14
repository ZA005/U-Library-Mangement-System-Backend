package com.university.librarymanagementsystem.repository.curriculum;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.curriculum.Program;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {

    @Query(value = "SELECT COUNT(*) FROM programs p WHERE p.prog_desc = :description", nativeQuery = true)
    Long countByProgDesc(@Param("description") String description);

    @Query(value = "SELECT * FROM programs WHERE dept_id = :departmentId", nativeQuery = true)
    List<Program> findByDepartmentId(@Param("departmentId") String departmentId);

    @Query(value = "SELECT * FROM program WHERE name = :programName", nativeQuery = true)
    Optional<Program> findByName(@Param("programName") String programName);
}