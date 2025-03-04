package com.university.librarymanagementsystem.dto.catalog;

import java.time.LocalDate;

import com.university.librarymanagementsystem.enums.BookStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCatalogDTO {

    private int id;
    private int bookId;
    private String callNumber;
    private String accessionNumber;
    private int conditionId;
    private LocalDate acquiredDate;
    private double purchasePrice;
    private BookStatus status;
    private int copies;
    private int locationId;
    private int sectionId;
    private String collectionType;
}
