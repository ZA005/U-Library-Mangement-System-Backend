package com.university.librarymanagementsystem.service.impl.circulation;

import com.university.librarymanagementsystem.dto.circulation.ReservationDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.entity.circulation.Reservation;
import com.university.librarymanagementsystem.entity.user.Account;
import com.university.librarymanagementsystem.enums.ReservationStatus;
import com.university.librarymanagementsystem.mapper.circulation.ReservationMapper;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.repository.circulation.ReservationRepository;
import com.university.librarymanagementsystem.repository.user.AccountRepository;
import com.university.librarymanagementsystem.service.circulation.ReservationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;
    private BookRepository bookRepository;
    private AccountRepository accountRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
            BookRepository bookRepository,
            AccountRepository accountRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public ReservationDTO createReservation(ReservationDTO reservationDTO) {
        Books book = bookRepository.findById(reservationDTO.getBook_id())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Account account = accountRepository.findById(reservationDTO.getAccount_id())
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        Reservation reservation = new Reservation();
        reservation.setBook(book);
        reservation.setAccount(account);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setExpirationDate(LocalDateTime.now().plusDays(1)); // Default 7-day expiration
        reservation.setStatus(ReservationStatus.PENDING);

        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationMapper.mapToReservationDTO(savedReservation);
    }

    @Override
    public ReservationDTO getReservationById(int id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        return ReservationMapper.mapToReservationDTO(reservation);
    }

    @Override
    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReservationMapper::mapToReservationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDTO> getReservationsByUserId(int accountId) {
        List<Reservation> reservations = reservationRepository.findAll()
                .stream()
                .filter(reservation -> reservation.getAccount().getAccount_id() == accountId)
                .collect(Collectors.toList());

        return reservations.stream()
                .map(ReservationMapper::mapToReservationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReservationDTO> getReservationsByBookId(int bookId) {
        List<Reservation> reservations = reservationRepository.findAll()
                .stream()
                .filter(reservation -> reservation.getBook().getId() == bookId)
                .collect(Collectors.toList());

        return reservations.stream()
                .map(ReservationMapper::mapToReservationDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationDTO updateReservationStatus(int id, String status) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        try {
            ReservationStatus reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
            reservation.setStatus(reservationStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid reservation status");
        }

        Reservation updatedReservation = reservationRepository.save(reservation);
        return ReservationMapper.mapToReservationDTO(updatedReservation);
    }

    @Override
    public void deleteReservation(int id) {
        if (!reservationRepository.existsById(id)) {
            throw new EntityNotFoundException("Reservation not found");
        }
        reservationRepository.deleteById(id);
    }
}
