package com.university.librarymanagementsystem.repository.catalog;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.dto.catalog.AccessionDTO;
import com.university.librarymanagementsystem.entity.catalog.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {

        @Query("SELECT b FROM Book b JOIN b.authors a WHERE a.name = :authorName")
        List<Book> findBooksByAuthorName(@Param("authorName") String authorName);

        @Query("SELECT b FROM Book b WHERE b.accessionNo LIKE ?1%")
        List<Book> findBooksByAccessionNo(String locationPrefix);

        Optional<Book> findByAccessionNo(String accessionNo);

        boolean existsByTitleAndIsbn10AndIsbn13(String title, String isbn10, String isbn13);

        Optional<Book> findTopByTitleAndIsbn10AndIsbn13OrderByAccessionNoDesc(String title, String isbn10,
                        String isbn13);

        Optional<Book> findTopByAccessionNoStartingWithOrderByAccessionNoDesc(String prefix);

        @Query(value = "SELECT accession_no FROM books ORDER BY id DESC LIMIT 1", nativeQuery = true)
        Optional<String> findLastAddedBookAccessionNumber();

        @Query("SELECT b FROM Book b " +
                        "WHERE (CAST(SUBSTRING(b.callNumber, 1, 3) AS integer) BETWEEN :ddcStart AND :ddcEnd) " +
                        "AND b.language = :language")
        List<Book> findByDdcAndLanguage(
                        @Param("ddcStart") int ddcStart,
                        @Param("ddcEnd") int ddcEnd,
                        @Param("language") String language);

        @Query("SELECT COALESCE(COUNT(b) - 1, 0) " +
                        "FROM Book b " +
                        "WHERE b.title = :title AND b.isbn10 = :isbn10 AND b.isbn13 = :isbn13 " +
                        "GROUP BY b.title, b.isbn10, b.isbn13 " +
                        "HAVING COUNT(b) > 1")
        Long getTotalDuplicatedBooks(@Param("title") String title,
                        @Param("isbn10") String isbn10,
                        @Param("isbn13") String isbn13);

        @Query(value = "SELECT bc.condition FROM book_condition bc WHERE bc.book_id = :bookId", nativeQuery = true)
        Optional<String> getBookConditionByBookId(@Param("bookId") Long bookId);

        @Modifying
        @Query(value = "INSERT INTO book_condition (book_id, `condition`) VALUES (:bookId, :condition)", nativeQuery = true)
        void saveBookCondition(@Param("bookId") Long bookId, @Param("condition") String condition);

        @Query("SELECT b FROM Book b WHERE b.accessionNo IS NOT NULL AND b.section IS NOT NULL")
        List<Book> findAllAccessionsWithSections();

        @Query(value = """
                        SELECT b.* FROM books b
                        LEFT JOIN book_reference br ON b.id = br.books_id AND br.course_id = :courseId
                        WHERE br.id IS NULL
                        AND b.id = (
                        SELECT MIN(b2.id)
                        FROM books b2
                        WHERE b2.title = b.title
                        )
                        """, nativeQuery = true)
        List<Book> findUniqueBooksNotInReference(@Param("courseId") Integer courseId);

        @Query(value = """
                        SELECT b.* FROM books b
                        WHERE b.id = (
                                SELECT MIN(b2.id)
                                FROM books b2
                                WHERE b2.title = b.title
                        )
                        """, nativeQuery = true)
        List<Book> findAllBooksUniqueOnly();

        List<Book> findBooksByIsbn13(String isbn13);

}
