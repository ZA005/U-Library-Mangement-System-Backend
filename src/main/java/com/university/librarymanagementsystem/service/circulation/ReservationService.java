package com.university.librarymanagementsystem.service.circulation;

import java.util.List;

import com.university.librarymanagementsystem.dto.circulation.BookLoanDetailsDTO;
import com.university.librarymanagementsystem.dto.circulation.ReservationDetailsDTO;

public interface ReservationService {

    List<ReservationDetailsDTO> getAllReservationDetails();

    BookLoanDetailsDTO getBookByAccessionNoReservation(String accessionNo);

    ReservationDetailsDTO saveReservation(ReservationDetailsDTO dto);

}
