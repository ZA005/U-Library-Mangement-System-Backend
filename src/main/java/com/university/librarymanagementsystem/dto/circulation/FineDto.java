package com.university.librarymanagementsystem.dto.circulation;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FineDto {

    private Long fineId;
    private Long loanId;
    private Long userId;
    private String stakeholder_id;
    private String first_name;
    private String last_name;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private Double fineAmount;
    private int paid;
}
