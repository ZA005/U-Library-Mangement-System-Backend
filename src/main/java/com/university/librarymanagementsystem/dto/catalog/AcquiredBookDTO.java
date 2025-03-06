package com.university.librarymanagementsystem.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcquiredBookDTO {

    private int id;
    private String purchase_date;
    private String acquired_date;
    private String vendor;
    private String vendor_location;
    private String funding_source;

}
