package com.university.librarymanagementsystem.mapper.circulation;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.entity.circulation.Reservation;

import com.university.librarymanagementsystem.dto.circulation.TransactionDTO;
import com.university.librarymanagementsystem.entity.circulation.Fine;
import com.university.librarymanagementsystem.entity.circulation.Loan;
import com.university.librarymanagementsystem.entity.circulation.TransactionHistory;

@Component
public class TransactionMapper {
    public static TransactionDTO mapToTransactionDTO(TransactionHistory transaction) {
        String userId = null;

        if (transaction.getLoan() != null) {
            userId = transaction.getLoan().getAccount().getUsername();
        } else if (transaction.getReservation() != null) {
            userId = transaction.getReservation().getAccount().getUsername();
        } else if (transaction.getFine() != null) {
            userId = transaction.getFine().getAccount().getUsername();
        }

        return new TransactionDTO(
                transaction.getId(),
                transaction.getTransactionType(),
                Optional.ofNullable(transaction.getLoan()).map(Loan::getId).orElse(null),
                Optional.ofNullable(transaction.getReservation()).map(Reservation::getId).orElse(null),
                Optional.ofNullable(transaction.getFine()).map(Fine::getId).orElse(null),
                userId,
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
