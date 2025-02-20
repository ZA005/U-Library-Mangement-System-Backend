package com.university.librarymanagementsystem.dto.circulation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowerDetailsDto {

    private String idNumber;
    private String department;
    private boolean hasCurrentBorrowedBook;
    private int reservationCount;
    private boolean registered;

}
