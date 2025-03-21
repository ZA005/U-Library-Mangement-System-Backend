package com.university.librarymanagementsystem.dto.circulation;

import java.time.LocalDateTime;

import com.university.librarymanagementsystem.enums.LoanStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {
    private int id;

    // BOOK
    private int book_id;
    private String book_accession_no;
    private String book_title;

    // ACCOUNT
    private int account_id;
    private String user_id;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String role;

    private LocalDateTime loanDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
}
