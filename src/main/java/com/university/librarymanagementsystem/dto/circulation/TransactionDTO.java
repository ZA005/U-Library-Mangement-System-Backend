package com.university.librarymanagementsystem.dto.circulation;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.university.librarymanagementsystem.enums.TransactionType;

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
public class TransactionDTO {
    private int id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private int loan_id;
    private int reservation_id;
    private int fine_id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime transDateTime;
}
