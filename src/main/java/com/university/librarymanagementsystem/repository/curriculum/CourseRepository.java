package com.university.librarymanagementsystem.repository.curriculum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.curriculum.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

        @Query(value = "SELECT c.* FROM courses c " +
                        "JOIN curriculum cu ON c.curr_id = cu.curr_id " +
                        "WHERE cu.program_id = :programId", nativeQuery = true)
        List<Course> findByProgramId(@Param("programId") Integer programId);

        @Query(value = "SELECT c.* FROM courses c " +
                        "JOIN curriculum cu ON c.curr_id = cu.curr_id " +
                        "WHERE cu.revision_no = :revisionNo", nativeQuery = true)
        List<Course> findByRevisionNo(@Param("revisionNo") Integer revisionNo);

}