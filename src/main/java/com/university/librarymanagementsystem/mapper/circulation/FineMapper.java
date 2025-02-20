package com.university.librarymanagementsystem.mapper.circulation;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.university.librarymanagementsystem.dto.circulation.FineDto;
import com.university.librarymanagementsystem.entity.circulation.Fine;
import com.university.librarymanagementsystem.entity.circulation.Loans;
import com.university.librarymanagementsystem.entity.user.Users;

public class FineMapper {

    // Convert Fine entity to FineDTO
    public static FineDto toDTO(Fine fine) {
        FineDto fineDTO = new FineDto();
        fineDTO.setFineId(fine.getFineId());
        fineDTO.setLoanId(fine.getLoan().getLoanId());
        fineDTO.setUserId(fine.getUser().getUserId());
        fineDTO.setBorrowDate(fine.getDueDate());
        fineDTO.setFineAmount(fine.getFineAmount());
        return fineDTO;
    }

    public static FineDto toFineDto(Object[] result) {
        return new FineDto(
                (Long) result[0], // fineId
                (Long) result[1], // loan Id
                (Long) result[2], // userId
                (String) result[3], // stakeholder_id
                (String) result[4], // first_name
                (String) result[5], // last_name
                convertTimestampToLocalDateTime(result[6]), // borrowDate
                convertTimestampToLocalDateTime(result[7]), // duedate
                convertTimestampToLocalDateTime(result[8]), // return date
                (Double) result[9], // funeamount
                (Integer) result[10] // paid

        );
    }

    // Convert FineDTO to Fine entity
    public static Fine toEntity(FineDto fineDTO) {
        Fine fine = new Fine();
        fine.setFineId(fineDTO.getFineId());
        Loans loan = new Loans();
        loan.setLoanId(fineDTO.getLoanId());

        // Set the user (Assumes you already have a user object available)
        Users user = new Users();
        user.setUserId(fineDTO.getUserId());
        fine.setUser(user);

        fine.setDueDate(fineDTO.getBorrowDate());
        fine.setFineAmount(fineDTO.getFineAmount());
        return fine;
    }

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

}
