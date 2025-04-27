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

    @Query(value = """
                SELECT
                    p.prog_desc AS `Program`,
                    c.year_level AS `Year Level`,
                    c.sem AS `Semester`,
                    c.course_code AS `Course Code`,
                    c.course_name AS `Course Name`,
                    b.title AS `Book Title`,
                    GROUP_CONCAT(a.name ORDER BY a.name ASC) AS `Author(s)`,
                    br.status AS `Copies`,
                    b.copyright AS `Copyright`
                FROM book_reference br
                INNER JOIN books b ON br.books_id = b.id
                INNER JOIN courses c ON br.course_id = c.course_id
                INNER JOIN curriculum cur ON c.curr_id = cur.curr_id
                INNER JOIN programs p ON cur.program_id = p.program_id
                LEFT JOIN author_books ab ON ab.book_id = b.id
                LEFT JOIN authors a ON a.id = ab.author_id
                WHERE p.status = 1
                AND cur.revision_no = :revisionNo
                AND p.program_id = :programId
                GROUP BY
                    p.prog_desc,
                    c.year_level,
                    c.sem,
                    c.course_code,
                    c.course_name,
                    b.title,
                    br.status,
                    b.copyright
                ORDER BY p.prog_desc, c.year_level, c.sem, c.course_code
            """, nativeQuery = true)
    List<Object[]> exportBookReferences(@Param("revisionNo") Integer revisionNo, @Param("programId") Integer programId);
}