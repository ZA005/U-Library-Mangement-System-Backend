package com.university.librarymanagementsystem.dto.circulation;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.university.librarymanagementsystem.enums.ReservationStatus;

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
public class ReservationDTO {
    private int id;

    private int book_id;
    private String book_title;
    private String book_accession_no;

    private int account_id;
    private String user_id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime reservationDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
