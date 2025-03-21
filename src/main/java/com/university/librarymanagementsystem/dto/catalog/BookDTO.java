package com.university.librarymanagementsystem.dto.catalog;

import java.time.LocalDate;
import java.util.List;

import com.university.librarymanagementsystem.enums.BookStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private int id;
    private String accessionNumber;
    private String title;
    private List<String> authors;
    private String isbn10;
    private String isbn13;
    private String description;
    private int pages;
    private String thumbnail;
    private String edition;
    private String series;
    private String language;
    private String publishedDate;
    private String publisher;
    private String copyRight;
    private String printType;
    private String format;
    @Enumerated(EnumType.STRING)
    private BookStatus status;
    private String condition;
    private BookCatalogDTO bookCatalog;

}
