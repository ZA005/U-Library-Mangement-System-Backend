package com.university.librarymanagementsystem.service.impl.circulation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.circulation.BorrowerDetailsDto;
import com.university.librarymanagementsystem.dto.circulation.LoanDetailsDTO;
import com.university.librarymanagementsystem.dto.circulation.LoanDto;
import com.university.librarymanagementsystem.dto.circulation.UserCirculationDetailsDTO;
import com.university.librarymanagementsystem.entity.catalog.Author;
import com.university.librarymanagementsystem.entity.catalog.Book;
import com.university.librarymanagementsystem.entity.circulation.Loans;
import com.university.librarymanagementsystem.entity.circulation.Reservation;
import com.university.librarymanagementsystem.entity.user.StakeHolders;
import com.university.librarymanagementsystem.entity.user.Users;
import com.university.librarymanagementsystem.enums.ReservationStatus;
import com.university.librarymanagementsystem.exception.ResourceNotFoundException;
import com.university.librarymanagementsystem.mapper.circulation.LoanMapper;
import com.university.librarymanagementsystem.mapper.user.StakeHolderMapper;
import com.university.librarymanagementsystem.repository.catalog.BookRepository;
import com.university.librarymanagementsystem.repository.circulation.FineRepository;
import com.university.librarymanagementsystem.repository.circulation.LoanRepository;
import com.university.librarymanagementsystem.repository.circulation.ReservationRepository;
import com.university.librarymanagementsystem.repository.user.StakeHolderRepository;
import com.university.librarymanagementsystem.repository.user.UserRepo;
import com.university.librarymanagementsystem.service.circulation.LoanService;
import com.university.librarymanagementsystem.service.user.EmailService;

@Service
public class LoanServiceImpl implements LoanService {

    // Use the mapper to set loan details from DTO
    ZoneId manilaZone = ZoneId.of("Asia/Manila");

    // Get the current date and time in Manila
    LocalDateTime nowInManila = LocalDateTime.now(manilaZone);

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private StakeHolderRepository stakeHolderRepository;

    private final EmailService emailService;

    public LoanServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public List<LoanDetailsDTO> getAllLoanDetails() {
        List<Loans> loans = loanRepository.findAll();
        return loans.stream()
                .map(loan -> {
                    LoanDetailsDTO dto = LoanMapper.mapToLoanDetailsDTO(loan);
                    StakeHolders stakeholder = stakeHolderRepository.findById(loan.getUser().getSchoolId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Stakeholder not found for user ID: " + loan.getUser().getUserId()));

                    dto.setDepartment(stakeholder.getDepartment().getName());

                    return dto;
                })
                .toList();
    }

    @Override
    public List<LoanDetailsDTO> getAllLoanWithBorrowedStatus() {
        List<Loans> loans = loanRepository.findByStatus("Borrowed");
        return loans.stream()
                .map(loan -> {
                    LoanDetailsDTO dto = LoanMapper.mapToLoanDetailsDTO(loan);

                    // Mapping from Book (Assuming Loans has a relationship with Book)
                    Book book = loan.getBook();
                    if (book != null) {
                        dto.setAccessionNo(book.getAccessionNo());
                        dto.setTitle(book.getTitle());
                        dto.setAuthor(book.getAuthors().stream()
                                .map(Author::getName).toList());
                        dto.setCallNum(book.getCallNumber());
                    }

                    // Mapping from Stakeholder (Assuming Loans has a relationship with Stakeholder)
                    Users user = loan.getUser();
                    StakeHolders stakeholder = stakeHolderRepository.findById(user.getSchoolId())
                            .orElseThrow(() -> new ResourceNotFoundException("No user"));

                    if (stakeholder != null) {
                        dto.setUncIdNumber(stakeholder.getId());
                        dto.setDepartment(stakeholder.getDepartment().getName());
                    }

                    return dto;
                })
                .toList();
    }

    @Override
    public BorrowerDetailsDto getBorrowerDetails(String id) {
        Users user = userRepo.findBySchoolId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user"));

        boolean isActivated = true;

        StakeHolders stakeHolder = stakeHolderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found: " + id));

        boolean hasCurrentBorrowedBook = loanRepository.findAll().stream()
                .anyMatch(loan -> loan.getUser().getSchoolId().equals(stakeHolder.getId()) &&
                        "borrowed".equalsIgnoreCase(loan.getStatus()));
        int reservationCount = reservationRepository.countByUserUserIdAndReservationStatus(user.getUserId(),
                ReservationStatus.PENDING);
        BorrowerDetailsDto borrowerDetailsDto = StakeHolderMapper.mapToBorrowerDetailsDto(stakeHolder);
        borrowerDetailsDto.setHasCurrentBorrowedBook(hasCurrentBorrowedBook);
        borrowerDetailsDto.setReservationCount(reservationCount);
        borrowerDetailsDto.setRegistered(isActivated);

        return borrowerDetailsDto;
    }

    @Override
    public Loans saveLoan(LoanDetailsDTO loanDetailsDTO) {
        // Find the book by accession number
        Book book = bookRepository.findByAccessionNo(loanDetailsDTO.getAccessionNo())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        // Check if the book is already loaned out
        if ("Loaned Out".equals(book.getStatus())) {
            throw new IllegalStateException("Book is already loaned out");
        }

        // Find the user by borrower ID (library card number)
        Users user = userRepo.findBySchoolId(loanDetailsDTO.getUncIdNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No user found with Id: " + loanDetailsDTO.getUncIdNumber()));

        StakeHolders stakeHolders = stakeHolderRepository.findById(user.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // Update book status to Loaned Out
        book.setStatus("Loaned Out");
        bookRepository.save(book);

        // Create a new Loans entity
        Loans loan = new Loans();
        loan.setBook(book);
        loan.setUser(user);

        // Set Borrow Date to current date and time in Manila
        loan.setBorrowDate(nowInManila);

        // Set Due Date
        LocalDateTime dueDateTime = nowInManila.atZone(manilaZone).toLocalDateTime();
        LocalDateTime newDueDate = dueDateTime.plusDays(1);
        loan.setDueDate(newDueDate);

        loan.setStatus("Borrowed");
        emailService.sendEmail(stakeHolders.getEmailAdd(), "Borrowed", book.getTitle(), loan.getDueDate().toString());
        // Save the loan record
        return loanRepository.save(loan);
    }

    @Override
    public LoanDetailsDTO getLoanDetails(Long loanId) {
        Loans loans = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("No loan details found" + loanId));
        return LoanMapper.mapToLoanDetailsDTO(loans);
    }

    @Override
    public LoanDto updateLoanStatus(Long loanId, LoanDto loanDto, String action) {
        // Find the loan by loan ID
        Loans loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        // Find the associated book by the loaned book's accession number
        Book book = loan.getBook();
        Users loanUser = loan.getUser();
        StakeHolders loanStakeHolders = stakeHolderRepository.findById(loanUser.getSchoolId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        // Handle "Returned" status updates
        if ("Returned".equals(loanDto.getStatus())) {
            // Update the return date to the current date if not provided
            loan.setReturnDate(loanDto.getReturnDate() != null ? loanDto.getReturnDate() : LocalDateTime.now());

            // Update the book status to "Available"
            book.setStatus("Available");
            bookRepository.save(book); // Save the updated book

            // Get the reservation
            Reservation reserves = reservationRepository.findFirstByBookAndReservationStatusOrderByReservationDateAsc(
                    book,
                    ReservationStatus.PENDING);
            if (reserves != null) {
                StakeHolders stakeHolders = stakeHolderRepository.findById(reserves.getUser().getSchoolId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                // Send an email to the user
                emailService.sendEmail(stakeHolders.getEmailAdd(), "Reserved", book.getTitle(), "");
            }
            // Email for returning
            emailService.sendEmail(loanStakeHolders.getEmailAdd(), "Returned", book.getTitle(), "");
        }
        // Handle "Renewed" status updates
        if ("Renewed".equals(action)) {
            loan.setBorrowDate(nowInManila);

            // Update the due date to one day after the new borrow date
            loan.setDueDate(nowInManila.plusDays(1));

            // Set the loan status to "Borrowed"
            loan.setStatus("Borrowed");

            // Update the book's status to "Loaned Out"
            book.setStatus("Loaned Out");
            bookRepository.save(book); // Save the updated book
            emailService.sendEmail(loanStakeHolders.getEmailAdd(), "Renewed", book.getTitle(),
                    loan.getDueDate().toString());
        }

        // Update the loan status in all cases
        loan.setStatus(loanDto.getStatus());

        // Save the updated loan record
        Loans updatedLoan = loanRepository.save(loan);

        LoanDto responseDto = new LoanDto();
        responseDto.setLoanId(updatedLoan.getLoanId());
        responseDto.setBookId(book.getId());
        responseDto.setBorrowDate(updatedLoan.getBorrowDate());
        responseDto.setReturnDate(updatedLoan.getReturnDate());
        responseDto.setDueDate(updatedLoan.getDueDate());
        responseDto.setStatus(updatedLoan.getStatus());
        return responseDto;
    }

    @Override
    public List<Loans> getOverdueLoans() {
        return loanRepository.findOverdueLoans(LocalDateTime.now());
    }

    @Override
    public boolean isBookLoanedByAccessionNo(String accessionNo) {

        List<Loans> activeLoans = loanRepository.findByBookAccessionNoAndStatus(accessionNo, "Borrowed");
        return !activeLoans.isEmpty(); // If there are active loans, the book is loaned out
    }

    @Override
    public UserCirculationDetailsDTO getUserCirculationDetails(String uncIdNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserCirculationDetails'");
    }

}
