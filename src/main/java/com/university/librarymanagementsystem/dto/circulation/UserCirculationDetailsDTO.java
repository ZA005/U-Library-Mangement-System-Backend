package com.university.librarymanagementsystem.dto.circulation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCirculationDetailsDTO {

    private int checkoutCount;
    private int reserveCount;
    private int fine;

}
