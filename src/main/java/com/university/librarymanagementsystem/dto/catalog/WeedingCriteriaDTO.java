package com.university.librarymanagementsystem.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeedingCriteriaDTO {

    private int id;
    private String ddcCategory;
    private Integer yearsBeforeWeeding;
    private String conditionThreshold;
    private Integer usageThreshold;
    private String language;
    private Boolean duplicationCheck;
    private Boolean criteriaStatus;
}
