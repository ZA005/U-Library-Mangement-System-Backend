package com.university.librarymanagementsystem.service.circulation;

import com.university.librarymanagementsystem.dto.circulation.ReservationDTO;
import java.util.List;

public interface ReservationService {

    // Create a new reservation
    ReservationDTO createReservation(ReservationDTO reservationDTO);

    // Get a reservation by ID
    ReservationDTO getReservationById(int id);

    // Get all reservations
    List<ReservationDTO> getAllReservations();

    // Get reservations by user ID
    List<ReservationDTO> getReservationsByUserId(int accountId);

    // Get reservations by book ID
    List<ReservationDTO> getReservationsByBookId(int bookId);

    // Update reservation status
    ReservationDTO updateReservationStatus(int id, String status);

    // Delete a reservation
    void deleteReservation(int id);
}
