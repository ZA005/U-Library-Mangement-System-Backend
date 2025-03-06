package com.university.librarymanagementsystem.dto.catalog;

import java.time.LocalDate;

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
    private String callNumber;
    private LocalDate acquiredDate;
    private double purchasePrice;
    private Integer copies;
    private int sectionId;
    private String collectionType;
}
