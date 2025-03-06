package com.university.librarymanagementsystem.repository.catalog;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.catalog.book.Books;

@Repository
public interface BookRepository extends JpaRepository<Books, Integer> {

    @Query("SELECT b FROM Books b JOIN b.authors a WHERE a.name = :authorName")
    List<Books> findBooksByAuthorName(@Param("authorName") String authorName);

    Optional<Books> findByIsbn13(String isbn13);
}
