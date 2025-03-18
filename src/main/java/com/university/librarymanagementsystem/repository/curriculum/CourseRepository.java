package com.university.librarymanagementsystem.repository.curriculum;

import java.util.List;
import java.util.Optional;

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

        @Query(value = "SELECT * FROM courses WHERE course_code = :courseCode", nativeQuery = true)
        List<Course> findByCourseCodeMultiple(@Param("courseCode") String courseCode);

        @Query(value = "SELECT * FROM courses WHERE course_code = :courseCode LIMIT 1", nativeQuery = true)
        Optional<Course> findByCourseCodeSingle(@Param("courseCode") String courseCode);

        @Query(value = "SELECT * FROM courses WHERE course_code = :courseCode AND course_name = :courseName AND year_level = :yearLevel AND sem = :sem LIMIT 1", nativeQuery = true)
        Optional<Course> findExistingCourse(@Param("courseCode") String courseCode,
                        @Param("courseName") String courseName,
                        @Param("yearLevel") int yearLevel,
                        @Param("sem") int sem);

        @Query(value = "SELECT * FROM courses WHERE course_code = :courseCode AND curr_id = :currId", nativeQuery = true)
        Optional<Course> findByCourseCodeAndCurriculum(@Param("courseCode") String courseCode,
                        @Param("currId") String currId);

}