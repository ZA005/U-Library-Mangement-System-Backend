package com.university.librarymanagementsystem.repository.circulation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.entity.circulation.Reservation;
import com.university.librarymanagementsystem.enums.ReservationStatus;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    Optional<Reservation> findFirstByBookAndStatusOrderByReservationDateAsc(Books book, ReservationStatus status);

    List<Reservation> findAllByOrderByIdDesc();
}
