package com.university.librarymanagementsystem.mapper.circulation;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.dto.circulation.ReservationDTO;
import com.university.librarymanagementsystem.entity.circulation.Reservation;
import com.university.librarymanagementsystem.entity.user.Account;

@Component
public class ReservationMapper {
    public static ReservationDTO mapToReservationDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getId(),
                reservation.getBook().getId(),
                reservation.getBook().getTitle(),
                reservation.getBook().getAccessionNumber(),
                reservation.getAccount().getAccount_id(),
                reservation.getAccount().getUsername(),
                reservation.getReservationDate(),
                reservation.getExpirationDate(),
                reservation.getStatus());
    }

    public static Reservation mapToReservation(ReservationDTO reservationDTO) {
        Books book = new Books();
        Account account = new Account();

        book.setId(reservationDTO.getBook_id());
        account.setAccount_id(reservationDTO.getAccount_id());
        return new Reservation(
                reservationDTO.getId(),
                book,
                account,
                reservationDTO.getReservationDateTime(),
                reservationDTO.getExpirationDate(),
                reservationDTO.getStatus());

    }
}
