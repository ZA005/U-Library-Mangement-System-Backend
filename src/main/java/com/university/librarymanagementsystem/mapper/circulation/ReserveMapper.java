package com.university.librarymanagementsystem.mapper.circulation;

import com.university.librarymanagementsystem.dto.circulation.ReservationDTO;
import com.university.librarymanagementsystem.dto.circulation.ReservationDetailsDTO;
import com.university.librarymanagementsystem.entity.catalog.Author;
import com.university.librarymanagementsystem.entity.circulation.Reservation;
import com.university.librarymanagementsystem.enums.ReservationStatus;

public class ReserveMapper {

    public static ReservationDTO mapToReservationDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getReservationId(),
                reservation.getBook().getId(),
                reservation.getUser().getUserId(),
                reservation.getReservationDate(),
                reservation.getExpirationDate(),
                reservation.getReservationStatus().name());
    }

    public static Reservation mapToReservationEntity(ReservationDTO dto) {
        Reservation reserve = new Reservation();
        reserve.setReservationId(dto.getReservationId());
        reserve.getBook().setId(dto.getBookId());
        reserve.getUser().setUserId(dto.getUserId());
        reserve.setReservationDate(dto.getReservationDate());
        reserve.setExpirationDate(dto.getExpirationDate());
        reserve.setReservationStatus(ReservationStatus.valueOf(dto.getReservationStatus()));
        return reserve;
    }

    public static ReservationDetailsDTO mapToReservationDetailsDTO(Reservation reservation) {
        ReservationDetailsDTO reserveDTo = new ReservationDetailsDTO();
        reserveDTo.setReservationId(reservation.getReservationId());
        reserveDTo.setReservationDate(reservation.getReservationDate());
        reserveDTo.setExpirationDate(reservation.getExpirationDate());
        reserveDTo.setReservationStatus(reservation.getReservationStatus().name());

        reserveDTo.setAccessionNo(reservation.getBook().getAccessionNo());
        reserveDTo.setTitle(reservation.getBook().getTitle());
        reserveDTo.setAuthor(reservation.getBook().getAuthors().stream()
                .map(Author::getName)
                .toList());
        reserveDTo.setCallNum(reservation.getBook().getCallNumber());

        reserveDTo.setUncIdNumber(reservation.getUser().getSchoolId());
        return reserveDTo;
    }

}
