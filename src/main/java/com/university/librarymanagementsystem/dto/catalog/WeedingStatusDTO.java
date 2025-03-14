package com.university.librarymanagementsystem.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeedingStatusDTO {

    private int id;
    private int bookId;
    private int weedingCriteriaId;
    private int weedingProcessId;
    private String weedStatus;
    private String reviewDate;
    private String notes;

}
