package com.university.librarymanagementsystem.mapper.catalog;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.catalog.BookCatalogDTO;
import com.university.librarymanagementsystem.entity.catalog.Acquisition;
import com.university.librarymanagementsystem.entity.catalog.BookCatalog;
import com.university.librarymanagementsystem.entity.catalog.Section;

@Component
public class BookCatalogMapper {

    public static BookCatalogDTO mapToBookCatalogDTO(BookCatalog bookCatalog) {
        return new BookCatalogDTO(
                bookCatalog.getId(),
                bookCatalog.getCallNumber(),
                bookCatalog.getCopies(),
                bookCatalog.getCollectionType(),
                SectionMapper.mapToSectionDTO(bookCatalog.getSection()),
                AcquisitionMapper.mapToAcquisitionDetailsDTO(bookCatalog.getAcquisition()));
    }

    public static BookCatalog toBookCatalogEntity(BookCatalogDTO bookCatalogDTO, Section section,
            Acquisition acquisition) {
        BookCatalog bookCatalog = new BookCatalog();
        bookCatalog.setId(bookCatalogDTO.getId());
        bookCatalog.setCallNumber(bookCatalogDTO.getCallNumber());
        bookCatalog.setCopies(bookCatalogDTO.getCopies());
        bookCatalog.setCollectionType(bookCatalogDTO.getCollectionType());
        bookCatalog.setSection(section);
        bookCatalog.setAcquisition(acquisition);

        return bookCatalog;
    }
}
