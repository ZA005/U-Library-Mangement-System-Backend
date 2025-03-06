package com.university.librarymanagementsystem.service.catalog;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Books;

public interface BookService {

    List<Books> saveBook(BookDTO bookDTO);

    List<BookDTO> fetchAllBooks();

    List<BookDTO> fetchBooksByAuthor(String authorName);

}
