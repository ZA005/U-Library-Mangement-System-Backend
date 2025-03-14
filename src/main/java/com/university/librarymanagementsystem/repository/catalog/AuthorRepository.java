package com.university.librarymanagementsystem.repository.catalog;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.catalog.book.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Optional<Author> findByName(String name);
}
