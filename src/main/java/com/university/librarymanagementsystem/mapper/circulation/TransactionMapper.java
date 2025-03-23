package com.university.librarymanagementsystem.mapper.circulation;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.entity.circulation.Reservation;

import com.university.librarymanagementsystem.dto.circulation.TransactionDTO;
import com.university.librarymanagementsystem.entity.circulation.Fine;
import com.university.librarymanagementsystem.entity.circulation.Loan;
import com.university.librarymanagementsystem.entity.circulation.TransactionHistory;

@Component
public class TransactionMapper {
    public static TransactionDTO mapToTransactionDTO(TransactionHistory transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getTransactionType(),
                transaction.getLoan().getId(),
                transaction.getReservation().getId(),
                transaction.getFine().getId(),
                transaction.getTransactionDate());
    }

    public static TransactionHistory mapToTransaction(TransactionDTO transactionDTO) {
        Loan loan = new Loan();
        Reservation reservation = new Reservation();
        Fine fine = new Fine();

        loan.setId(transactionDTO.getLoan_id());
        reservation.setId(transactionDTO.getReservation_id());
        fine.setId(transactionDTO.getFine_id());
        return new TransactionHistory(
                transactionDTO.getId(),
                transactionDTO.getType(),
                loan,
                reservation,
                fine,
                transactionDTO.getTransDateTime());
    }
}
