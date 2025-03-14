package com.university.librarymanagementsystem.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcquisitionDTO {
    private int id;
    private String book_title;
    private String isbn;
    private String publisher;
    private String edition;
    private String published_date;
    private float purchase_price;
    private String purchase_date;
    private String acquired_date;
    private String vendor;
    private String vendor_location;
    private String funding_source;
    private int status;
}
