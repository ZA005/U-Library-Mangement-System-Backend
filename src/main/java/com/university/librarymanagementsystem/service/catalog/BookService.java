package com.university.librarymanagementsystem.service.catalog;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.BookCatalogDTO;
import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.entity.catalog.BookCatalog;
import com.university.librarymanagementsystem.entity.catalog.book.Books;

public interface BookService {

    // BookCatalog saveBook(BookCatalogDTO bookCatalogDTO);
    List<Books> saveBook(BookDTO bookDTO);

    // List<BookCatalogDTO> fetchAllBooks();
    List<BookDTO> fetchAllBooks();

    List<BookCatalogDTO> fetchBooksByAuthor(String authorName);

}
