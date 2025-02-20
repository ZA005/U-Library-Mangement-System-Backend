package com.university.librarymanagementsystem.controller.circulation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.circulation.BookLoanDetailsDTO;
import com.university.librarymanagementsystem.dto.circulation.ReservationDetailsDTO;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.service.circulation.ReservationService;

@RestController
@RequestMapping("/adminuser/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @GetMapping("/all-reservations")
    public ResponseEntity<List<ReservationDetailsDTO>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservationDetails());
    }

    @GetMapping("/accessionNo/{accessionNo}")
    public ResponseEntity<BookLoanDetailsDTO> getBookDetailsForReservation(@PathVariable String accessionNo) {
        try {
            BookLoanDetailsDTO book = reservationService.getBookByAccessionNoReservation(accessionNo);
            return ResponseEntity.ok(book);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/save-reservation")
    public ResponseEntity<ReservationDetailsDTO> saveReservation(
            @RequestBody ReservationDetailsDTO reservationDetailsDTO) {
        return ResponseEntity.ok(reservationService.saveReservation(reservationDetailsDTO));
    }

}
