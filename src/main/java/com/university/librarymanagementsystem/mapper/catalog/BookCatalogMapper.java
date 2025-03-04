package com.university.librarymanagementsystem.mapper.catalog;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.BookCatalogDTO;
import com.university.librarymanagementsystem.entity.catalog.BookCatalog;
import com.university.librarymanagementsystem.entity.catalog.book.Books;

@Component
public class BookCatalogMapper {

    public static BookCatalogDTO toBookCatalogDTO(BookCatalog bookCatalog) {
        return new BookCatalogDTO(
                bookCatalog.getId(),
                bookCatalog.getBookId().getId(),
                bookCatalog.getCallNumber(),
                bookCatalog.getAccessionNumber(),
                bookCatalog.getConditionId().getId(),
                bookCatalog.getAcquiredDate(),
                bookCatalog.getPurchasePrice(),
                bookCatalog.getStatus(),
                bookCatalog.getCopies(),
                bookCatalog.getLocationId().getId(),
                bookCatalog.getSectionId().getId(),
                bookCatalog.getCollectionType());
    }

    public static BookCatalog toBookCatalog(BookCatalogDTO bookCatalogDTO, Books book) {
        BookCatalog bookCatalog = new BookCatalog();
        bookCatalog.setId(bookCatalogDTO.getId());
        bookCatalog.setBookId(book);
        bookCatalog.setCallNumber(bookCatalogDTO.getCallNumber());
        bookCatalog.setAccessionNumber(bookCatalogDTO.getAccessionNumber());
        bookCatalog.setAcquiredDate(bookCatalogDTO.getAcquiredDate());
        bookCatalog.setPurchasePrice(bookCatalogDTO.getPurchasePrice());
        bookCatalog.setStatus(bookCatalogDTO.getStatus());
        bookCatalog.setCopies(bookCatalogDTO.getCopies());
        bookCatalog.setCollectionType(bookCatalogDTO.getCollectionType());
        return bookCatalog;
    }
}
