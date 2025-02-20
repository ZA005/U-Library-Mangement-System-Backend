package com.university.librarymanagementsystem.dto.catalog;

import java.time.LocalDate;

import com.university.librarymanagementsystem.enums.ProcessStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookWeedingDTO {

    private Long id;
    private LocalDate starDate;
    private LocalDate endDate;
    private ProcessStatus processStatus;
    private String initiator;
    private String notes;
}