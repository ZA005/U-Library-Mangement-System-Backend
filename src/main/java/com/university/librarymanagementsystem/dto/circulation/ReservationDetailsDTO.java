package com.university.librarymanagementsystem.dto.circulation;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailsDTO {
    // Reserve
    private Long reservationId;
    private LocalDateTime reservationDate;
    private LocalDateTime expirationDate;
    private String reservationStatus;
    // Book
    private String accessionNo;
    private String title;
    private List<String> author;
    private String callNum;

    // Stakeholder
    private String uncIdNumber;
    private String department;

}
