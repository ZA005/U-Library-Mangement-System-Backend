package com.university.librarymanagementsystem.repository.catalog;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.repository.catalog.customrepository.BookCustomRepository;

@Repository
public interface BookRepository extends JpaRepository<Books, Integer>, BookCustomRepository {

	@Query("SELECT b FROM Books b JOIN b.authors a WHERE a.name = :authorName")
	List<Books> findBooksByAuthorName(@Param("authorName") String authorName);

	List<Books> findByIsbn13(String isbn13);

	boolean existsByIsbn13(String isbn13);

	Optional<Books> findTopByIsbn13OrderByAccessionNumberDesc(String isbn13);

	Optional<Books> findTopByAccessionNumberStartingWithOrderByAccessionNumberDesc(String accessionNumber);

	@Query("SELECT b FROM Books b JOIN b.bookCatalog bc " +
			"WHERE (CAST(SUBSTRING(bc.callNumber, 1, 3) AS integer) BETWEEN :ddcStart AND :ddcEnd) " +
			"AND b.language = :language")
	List<Books> findBooksByLanguageAndCallNumberRange(
			@Param("ddcStart") int ddcStart,
			@Param("ddcEnd") int ddcEnd,
			@Param("language") String language);

	@Query("SELECT COALESCE(COUNT(b) - 1, 0) " +
			"FROM Books b " +
			"WHERE b.isbn13 = :isbn13 " +
			"GROUP BY b.isbn13 " +
			"HAVING COUNT(b) > 1")
	Long getTotalDuplicatedBooks(@Param("isbn13") String isbn13);

	@Query(value = """
			SELECT b.* FROM books b
			JOIN book_catalog bc ON b.catalog_id = bc.id
			JOIN acquisition a ON bc.acquisition_id = a.id
			ORDER BY STR_TO_DATE(a.acquired_date, '%Y-%m-%d') DESC
			""", nativeQuery = true)
	List<Books> findAllNewlyAcquiredBooks();

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
	List<Books> findUniqueBooksNotInReference(@Param("courseId") Integer courseId);

	@Query(value = """
			SELECT b.* FROM books b
			WHERE b.id = (
			SELECT MIN(b2.id)
			FROM books b2
			WHERE b2.title = b.title
			)
			""", nativeQuery = true)
	List<Books> findAllBooksUniqueOnly();
}
