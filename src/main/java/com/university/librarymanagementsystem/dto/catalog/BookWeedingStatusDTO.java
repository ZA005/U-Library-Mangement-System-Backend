package com.university.librarymanagementsystem.dto.catalog;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookWeedingStatusDTO {

    private Long id;
    private Long bookId;
    private Integer weedingCriteriaId;
    private Long bookWeedingId;
    private String weedStatus;
    private LocalDate reviewDate;
    private String notes;
}
