package com.university.librarymanagementsystem.repository.curriculum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.university.librarymanagementsystem.entity.curriculum.Curriculum;

public interface CurriculumRepository extends JpaRepository<Curriculum, String> {

    @Query(value = "SELECT * FROM curriculum WHERE program_id = :programId", nativeQuery = true)
    List<Curriculum> findByProgramId(@Param("programId") Integer programId);

    @Query(value = "SELECT DISTINCT revision_no FROM curriculum WHERE program_id = :programId", nativeQuery = true)
    List<Integer> findRevisionsByProgramId(@Param("programId") Integer programId);

}