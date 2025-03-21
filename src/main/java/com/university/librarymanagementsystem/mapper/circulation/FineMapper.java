package com.university.librarymanagementsystem.mapper.circulation;

import com.university.librarymanagementsystem.dto.circulation.FineDTO;
import com.university.librarymanagementsystem.entity.circulation.Fine;
import com.university.librarymanagementsystem.entity.circulation.Loan;
import com.university.librarymanagementsystem.entity.user.Account;

import org.springframework.stereotype.Component;

@Component
public class FineMapper {

    public static FineDTO mapToFineDTO(Fine fine) {
        return new FineDTO(
                fine.getId(),
                fine.getLoan().getId(),
                fine.getAccount().getAccount_id(),
                fine.getAccount().getUsername(),
                fine.getFine_amount(),
                fine.getStatus(),
                fine.getFineDate(),
                fine.getPaymentDate());
    }

    public static Fine mapToFine(FineDTO fineDTO) {
        Loan loan = new Loan();
        Account account = new Account();

        loan.setId(fineDTO.getLoan_id());
        account.setAccount_id(loan.getAccount().getAccount_id());
        return new Fine(
                fineDTO.getId(),
                loan,
                account,
                fineDTO.getFine_amount(),
                fineDTO.getStatus(),
                fineDTO.getFineDate(),
                fineDTO.getPaymentDate());
    }
}
