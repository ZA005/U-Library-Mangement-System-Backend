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
public class BookDto {

    private Long id;
    private String title;
    private String accessionNo;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private Integer pageCount;
    private String categories;
    private String language;
    private String isbn10;
    private String isbn13;
    private String thumbnail;
    private String printType;
    private String status;
    private String callNumber;
    private Double purchasePrice;
    private String section;
    private LocalDate dateAcquired;
    private String notes;
    private String location;
    private String vendor;
    private String fundingSource;
    private String subjects;
    private String collectionType;
    private String bookCondition;

}
