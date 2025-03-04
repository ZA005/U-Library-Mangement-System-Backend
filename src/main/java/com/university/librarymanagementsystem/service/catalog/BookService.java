package com.university.librarymanagementsystem.service.catalog;

import com.university.librarymanagementsystem.dto.catalog.BookCatalogDTO;
import com.university.librarymanagementsystem.dto.catalog.BookDTO;
import com.university.librarymanagementsystem.entity.catalog.BookCatalog;
import com.university.librarymanagementsystem.entity.catalog.book.Books;

public interface BookService {

    Books saveBook(BookDTO bookDTO, BookCatalogDTO bookCatalogDTO);

    BookCatalog getSavedBookCatalog(Books book);

}
