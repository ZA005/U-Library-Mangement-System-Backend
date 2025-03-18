package com.university.librarymanagementsystem.repository.curriculum;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.curriculum.BookReference;

@Repository
public interface BookReferenceRepository extends JpaRepository<BookReference, Integer> {

    @Query(value = "SELECT * FROM book_reference WHERE course_id = :courseId", nativeQuery = true)
    List<BookReference> findAllBookReferenceByCourse(@Param("courseId") Integer courseId);

    @Modifying
    @Query(value = "DELETE FROM book_reference WHERE course_id IN (SELECT course_id FROM courses WHERE course_code = :courseCode)", nativeQuery = true)
    void deleteByCourseCode(@Param("courseCode") String courseCode);

    @Modifying
    @Query(value = "DELETE FROM book_reference WHERE course_id IN "
            + "(SELECT course_id FROM courses WHERE course_code = :courseCode) "
            + "AND books_id IN (SELECT id FROM books WHERE title = :bookName)", nativeQuery = true)
    void deleteByCourseCodeAndBookName(@Param("courseCode") String courseCode, @Param("bookName") String bookName);

    @Query(value = "SELECT COUNT(*) FROM book_reference WHERE course_id = :courseId AND books_id = :bookId", nativeQuery = true)
    Long countByCourseIdAndBookId(@Param("courseId") Integer courseId, @Param("bookId") Integer bookId);

}