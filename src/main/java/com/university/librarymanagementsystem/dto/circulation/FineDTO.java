package com.university.librarymanagementsystem.dto.circulation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FineDTO {

    private int id;

    private int loan_id;

    private int account_id;
    private String user_id;

    private BigDecimal fine_amount;

    private byte status;

    private LocalDateTime fineDate;

    private LocalDateTime paymentDate;
}
