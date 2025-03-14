package com.university.librarymanagementsystem.entity.catalog;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "weeding_criteria")
public class WeedingCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ddc_category", length = 3, nullable = false)
    private String ddcCategory;

    @Column(name = "years_before_weeding")
    private int yearsBeforeWeeding;

    @Column(name = "condition_threshold", length = 20)
    private String conditionThreshold;

    @Column(name = "usage_threshold")
    private Integer usageThreshold;

    @Column(name = "language", length = 50)
    private String language;

    @Column(name = "duplication_check")
    private Boolean duplicationCheck = true;

    @Column(name = "criteria_status")
    private Boolean criteriaStatus = true;

}
