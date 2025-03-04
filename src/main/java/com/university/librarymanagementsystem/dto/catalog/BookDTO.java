package com.university.librarymanagementsystem.dto.catalog;

import java.time.LocalDate;
import java.util.List;

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
    private LocalDate publishedDate;
    private String publisher;
    private LocalDate copyRight;
    private String printType;
    private String format;

}
