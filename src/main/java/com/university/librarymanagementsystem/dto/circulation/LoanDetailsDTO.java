package com.university.librarymanagementsystem.dto.circulation;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanDetailsDTO {
    // Loans
    private Long id;
    private LocalDateTime dateBorrowed;
    private LocalDateTime dueDate;
    private LocalDateTime dateReturned;
    private String loanStatus;
    // Book
    private String accessionNo;
    private String title;
    private List<String> author;
    private String callNum;
    // Stakeholder
    private String uncIdNumber;
    private String department;

}
