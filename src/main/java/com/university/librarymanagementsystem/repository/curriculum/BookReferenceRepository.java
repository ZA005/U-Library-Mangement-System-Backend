package com.university.librarymanagementsystem.repository.curriculum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.curriculum.BookReference;

@Repository
public interface BookReferenceRepository extends JpaRepository<BookReference, Integer> {

    @Query(value = "SELECT * FROM book_reference WHERE course_id = :courseId", nativeQuery = true)
    List<BookReference> findAllBookReferenceByCourse(@Param("courseId") Integer courseId);
}