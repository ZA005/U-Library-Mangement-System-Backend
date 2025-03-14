package com.university.librarymanagementsystem.repository.catalog.customrepository;

import java.util.List;

import com.university.librarymanagementsystem.dto.catalog.BookSearchRequestDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Books;

public interface BookCustomRepository {

    List<Books> advanceSearchBooks(BookSearchRequestDTO bookSearchRequestDTO);
}
