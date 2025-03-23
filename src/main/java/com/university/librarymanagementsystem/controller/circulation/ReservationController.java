package com.university.librarymanagementsystem.controller.circulation;

import com.university.librarymanagementsystem.dto.circulation.ReservationDTO;
import com.university.librarymanagementsystem.service.circulation.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adminuser/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = reservationService.createReservation(reservationDTO);
        return ResponseEntity.ok(createdReservation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable int id) {
        ReservationDTO reservation = reservationService.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/user/{accountId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByUserId(@PathVariable int accountId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByUserId(accountId);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByBookId(@PathVariable int bookId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByBookId(bookId);
        return ResponseEntity.ok(reservations);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ReservationDTO> updateReservationStatus(@PathVariable int id, @RequestParam String status) {
        ReservationDTO updatedReservation = reservationService.updateReservationStatus(id, status);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable int id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
