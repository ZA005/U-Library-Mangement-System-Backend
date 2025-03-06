package com.university.librarymanagementsystem.repository.catalog;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.catalog.BookCatalog;
import com.university.librarymanagementsystem.entity.catalog.Section;
import com.university.librarymanagementsystem.entity.catalog.book.Books;

@Repository
public interface BookCatalogRepository extends JpaRepository<BookCatalog, Integer> {

    // Optional<BookCatalog> findByBook(Books book);

    // Optional<BookCatalog> findByBookAndSection(Books book, Section section);

}
