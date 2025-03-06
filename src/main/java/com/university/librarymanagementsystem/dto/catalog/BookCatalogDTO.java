package com.university.librarymanagementsystem.dto.catalog;

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
    private Integer copies;
    private String collectionType;
    private SectionDTO section;
    private AcquisitionDetailsDTO acquisitionDetails;

}
