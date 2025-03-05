package com.university.librarymanagementsystem.mapper.catalog;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.BookCatalogDTO;
import com.university.librarymanagementsystem.entity.catalog.BookCatalog;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.entity.catalog.book.Condition;

@Component
public class BookCatalogMapper {

    public static BookCatalogDTO mapToBookCatalogDTO(BookCatalog bookCatalog) {
        return new BookCatalogDTO(
                bookCatalog.getId(),
                bookCatalog.getBook().getId(),
                bookCatalog.getCallNumber(),
                bookCatalog.getAccessionNumber(),
                bookCatalog.getCondition().getId(),
                bookCatalog.getAcquiredDate(),
                bookCatalog.getPurchasePrice(),
                bookCatalog.getStatus(),
                bookCatalog.getCopies(),
                bookCatalog.getLocation().getId(),
                bookCatalog.getSection().getId(),
                bookCatalog.getCollectionType());
    }

    public static BookCatalog mapToBookCatalog(BookCatalogDTO bookCatalogDTO, Books book) {
        BookCatalog bookCatalog = new BookCatalog();
        bookCatalog.setId(bookCatalogDTO.getId());
        bookCatalog.setBook(book);
        bookCatalog.setCallNumber(bookCatalogDTO.getCallNumber());
        bookCatalog.setAccessionNumber(bookCatalogDTO.getAccessionNumber());
        bookCatalog.setStatus(bookCatalogDTO.getStatus());
        bookCatalog.setCopies(bookCatalogDTO.getCopies());
        bookCatalog.setCollectionType(bookCatalogDTO.getCollectionType());
        return bookCatalog;
    }
}
