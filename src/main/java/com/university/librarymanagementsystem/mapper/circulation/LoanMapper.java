package com.university.librarymanagementsystem.mapper.circulation;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.circulation.LoanDTO;
import com.university.librarymanagementsystem.entity.catalog.book.Books;
import com.university.librarymanagementsystem.entity.circulation.Loan;
import com.university.librarymanagementsystem.entity.user.Account;

@Component
public class LoanMapper {
    public static LoanDTO mapToLoanDTO(Loan loan) {
        String fullName = loan.getAccount().getUsers().getFirstName() + " " + loan.getAccount().getUsers().getLastName()
                + " " + loan.getAccount().getUsers().getSuffix();
        return new LoanDTO(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getAccessionNumber(),
                loan.getBook().getTitle(),
                loan.getAccount().getAccount_id(),
                loan.getAccount().getUsers() != null ? loan.getAccount().getUsers().getId() : null,
                loan.getAccount().getUsers() != null ? loan.getAccount().getUsers().getEmailAdd() : null,
                fullName,
                loan.getAccount().getUsers() != null ? loan.getAccount().getUsers().getSuffix() : null,
                loan.getAccount().getRole(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate(),
                loan.getStatus());
    }

    public static Loan mapToLoan(LoanDTO loanDTO) {
        Books book = new Books();
        Account account = new Account();

        book.setId(loanDTO.getBook_id());
        account.setAccount_id(loanDTO.getAccount_id());

        return new Loan(
                loanDTO.getId(),
                book,
                account,
                loanDTO.getLoanDate(),
                loanDTO.getDueDate(),
                loanDTO.getReturnDate(),
                loanDTO.getStatus());
    }
}
