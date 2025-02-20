package com.university.librarymanagementsystem.mapper.circulation;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.university.librarymanagementsystem.dto.circulation.LoanDetailsDTO;
import com.university.librarymanagementsystem.dto.circulation.LoanDto;
import com.university.librarymanagementsystem.entity.catalog.Author;
import com.university.librarymanagementsystem.entity.circulation.Loans;

public class LoanMapper {

    // public static LoanDto toLoanDto(Object[] result) {
    // return new LoanDto(
    // (Long) result[0], // loanId
    // (String) result[1], // accessionNo
    // (String) result[2], // title
    // (String) result[3], // callNumber
    // (String) result[4], // authorName
    // (String) result[5], // borrowerLibraryCardNo
    // (String) result[6], // departmentName
    // convertTimestampToLocalDateTime(result[7]), // borrowDate
    // convertTimestampToLocalDateTime(result[8]), // returnDate
    // convertTimestampToLocalDateTime(result[9]), // due
    // (String) result[10] // status
    // );
    // }

    // Helper method to convert Timestamp to LocalDateTime
    private static LocalDateTime convertTimestampToLocalDateTime(Object timestampObj) {
        if (timestampObj == null) {
            return null; // Handle null values gracefully
        }

        // Ensure the object is of type Timestamp
        if (timestampObj instanceof Timestamp) {
            return ((Timestamp) timestampObj).toLocalDateTime();
        }

        return null; // Return null if the object is not a Timestamp (fallback)
    }


    public static LoanDto mapToLoanDto(Loans loan) {
        LoanDto dto = new LoanDto();
        dto.setLoanId(loan.getLoanId());
        dto.setBookId(loan.getBook().getId());
        dto.setUserId(loan.getUser().getUserId());
        dto.setBorrowDate(loan.getBorrowDate());
        dto.setReturnDate(loan.getReturnDate());
        dto.setDueDate(loan.getDueDate());
        return dto;
    }

    public static Loans mapToLoans(LoanDto dto) {
        Loans loans = new Loans();
        loans.setLoanId(dto.getLoanId());
        loans.getBook().setId(dto.getBookId());
        loans.getUser().setUserId(dto.getUserId());
        loans.setBorrowDate(dto.getBorrowDate());
        loans.setReturnDate(dto.getReturnDate());
        loans.setDueDate(dto.getDueDate());
        loans.setStatus(dto.getStatus());
        return loans;
    }

    public static LoanDetailsDTO mapToLoanDetailsDTO(Loans loans) {
        LoanDetailsDTO dto = new LoanDetailsDTO();

        dto.setId(loans.getLoanId());
        dto.setDateBorrowed(loans.getBorrowDate());
        dto.setDueDate(loans.getDueDate());
        dto.setDateReturned(loans.getReturnDate());
        dto.setLoanStatus(loans.getStatus());

        dto.setAccessionNo(loans.getBook().getAccessionNo());
        dto.setTitle(loans.getBook().getTitle());
        dto.setAuthor(loans.getBook().getAuthors().stream()
                .map(Author::getName)
                .toList());
        dto.setCallNum(loans.getBook().getCallNumber());
        dto.setUncIdNumber(loans.getUser().getSchoolId());
        // dto.setDepartment(loans.getUser().getStakeholder().getDepartment().getName());

        return dto;
    }

}

