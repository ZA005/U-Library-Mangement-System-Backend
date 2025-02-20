package com.university.librarymanagementsystem.dto.circulation;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookLoanDetails {

    private String title;
    private String callNumber;
    private List<String> authors;
    private String bookStatus;
}
