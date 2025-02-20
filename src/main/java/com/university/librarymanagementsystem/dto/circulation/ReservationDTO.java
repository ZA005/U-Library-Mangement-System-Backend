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
public class ReservationDTO {

    private Long reservationId;
    private Long bookId;
    private Long userId;
    private LocalDateTime reservationDate;
    private LocalDateTime expirationDate;
    private String reservationStatus;
}
