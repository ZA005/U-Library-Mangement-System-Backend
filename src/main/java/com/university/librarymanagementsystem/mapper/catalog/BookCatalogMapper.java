package com.university.librarymanagementsystem.mapper.catalog;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.BookCatalogDTO;
import com.university.librarymanagementsystem.entity.catalog.BookCatalog;
import com.university.librarymanagementsystem.entity.catalog.Section;

@Component
public class BookCatalogMapper {

    public static BookCatalogDTO mapToBookCatalogDTO(BookCatalog bookCatalog) {
        return new BookCatalogDTO(
                bookCatalog.getId(),
                bookCatalog.getCallNumber(),
                bookCatalog.getAcquiredDate(),
                bookCatalog.getPurchasePrice(),
                bookCatalog.getCopies(),
                bookCatalog.getSection().getId(),
                bookCatalog.getCollectionType());
    }

    public static BookCatalog toBookCatalogEntity(BookCatalogDTO bookCatalogDTO, Section section) {
        BookCatalog bookCatalog = new BookCatalog();
        bookCatalog.setId(bookCatalogDTO.getId());
        bookCatalog.setCallNumber(bookCatalogDTO.getCallNumber());
        bookCatalog.setAcquiredDate(bookCatalogDTO.getAcquiredDate());
        bookCatalog.setPurchasePrice(bookCatalogDTO.getPurchasePrice());
        bookCatalog.setCopies(bookCatalogDTO.getCopies());
        bookCatalog.setSection(section);
        bookCatalog.setCollectionType(bookCatalogDTO.getCollectionType());
        return bookCatalog;
    }
}
