package com.university.librarymanagementsystem.repository.catalog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.university.librarymanagementsystem.entity.catalog.book.Categories;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {

    @Query(value = """
            SELECT c.*
            FROM categories c
            WHERE c.name LIKE CONCAT('%', :name, '%')
            ORDER BY CASE WHEN c.name LIKE CONCAT('%', :name, '%') THEN 1 ELSE 0 END DESC
            LIMIT 1""", nativeQuery = true)
    Categories findBestMatchByName(@Param("name") String name);
}
