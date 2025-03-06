package com.university.librarymanagementsystem.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcquisitionDetailsDTO {

    private int id;
    private String purchase_date;
    private float purchase_price;
    private String acquired_date;
    private String vendor;
    private String vendor_location;
    private String funding_source;

}
