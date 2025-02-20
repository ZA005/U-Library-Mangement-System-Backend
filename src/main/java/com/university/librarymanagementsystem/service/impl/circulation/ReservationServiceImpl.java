package com.university.librarymanagementsystem.service.impl.circulation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.circulation.BookLoanDetailsDTO;
import com.university.librarymanagementsystem.dto.circulation.ReservationDetailsDTO;
import com.university.librarymanagementsystem.entity.catalog.Book;
import com.university.librarymanagementsystem.entity.circulation.Loans;
import com.university.librarymanagementsystem.entity.circulation.Reservation;
import com.university.librarymanagementsystem.entity.user.StakeHolders;
import com.university.librarymanagementsystem.entity.user.Users;
import com.university.librarymanagementsystem.enums.ReservationStatus;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.catalog.BookMapper;
import com.university.librarymanagementsystem.mapper.circulation.ReserveMapper;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.repository.circulation.ReservationRepository;
import com.university.librarymanagementsystem.repository.user.StakeHolderRepository;
import com.university.librarymanagementsystem.repository.user.UserRepo;
import com.university.librarymanagementsystem.service.circulation.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private StakeHolderRepository stakeHolderRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepo userRepo;

    // Use the mapper to set loan details from DTO
    ZoneId manilaZone = ZoneId.of("Asia/Manila");

    // Get the current date and time in Manila
    LocalDateTime nowInManila = LocalDateTime.now(manilaZone);

    @Override
    public List<ReservationDetailsDTO> getAllReservationDetails() {
        List<Reservation> reserves = reservationRepository.findAll();
        return reserves.stream()
                .map(reservation -> {
                    ReservationDetailsDTO dto = ReserveMapper.mapToReservationDetailsDTO(reservation);
                    StakeHolders stakeHolder = stakeHolderRepository.findById(reservation.getUser().getSchoolId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Stakeholder not found for user ID: " + reservation.getUser().getUserId()));
                    dto.setDepartment(stakeHolder.getDepartment().getName());
                    return dto;
                })
                .toList();
    }

    @Override
    public BookLoanDetailsDTO getBookByAccessionNoReservation(String accessionNo) {
        Book book = bookRepository.findByAccessionNo(accessionNo)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No book found with accession number: " + accessionNo));
        // Check if the book status is ARCHIVED, WEEDED, or LOST
        if ("ARCHIVED".equalsIgnoreCase(book.getStatus()) ||
                "WEEDED".equalsIgnoreCase(book.getStatus()) ||
                "LOST".equalsIgnoreCase(book.getStatus())) {
            throw new ResourceNotFoundException("The book is not available for borrowing. Status: " + book.getStatus());
        }
        return BookMapper.mapToBookLoanDetails(book);
    }

    @Override
    public ReservationDetailsDTO saveReservation(ReservationDetailsDTO dto) {
        Book book = bookRepository.findByAccessionNo(dto.getAccessionNo())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        Users user = userRepo.findBySchoolId(dto.getUncIdNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No user found with Id: " + dto.getUncIdNumber()));

        // book.setStatus(dto.getReservationStatus());

        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setUser(user);

        reservation.setReservationDate(nowInManila);

        LocalDateTime expirationDate = nowInManila.atZone(manilaZone).toLocalDateTime();
        LocalDateTime newExpirationDate = expirationDate.plusHours(5);
        reservation.setExpirationDate(newExpirationDate);

        reservation.setReservationStatus(ReservationStatus.PENDING);

        bookRepository.save(book);
        reservationRepository.save(reservation);
        return dto;
    }

}
