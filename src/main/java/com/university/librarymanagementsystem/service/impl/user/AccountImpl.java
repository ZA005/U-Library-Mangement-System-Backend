package com.university.librarymanagementsystem.service.impl.user;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.user.AccountDTO;
import com.university.librarymanagementsystem.entity.user.Account;
import com.university.librarymanagementsystem.mapper.user.AccountMapper;
import com.university.librarymanagementsystem.repository.circulation.LoanRepository;
import com.university.librarymanagementsystem.repository.user.AccountRepository;
import com.university.librarymanagementsystem.service.user.AccountService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountImpl implements AccountService {
    private LoanRepository loanRepo;
    private AccountRepository accountRepo;

    @Override
    public AccountDTO fetchAccountByID(String user_id) {
        Account account = accountRepo.findByUserID(user_id).orElse(null);
        if (account == null) {
            return null;
        }
        System.out.println(account.getAccount_id());
        if (!checkAccountLoan(account.getAccount_id())) {
            throw new IllegalStateException("User has reached the borrowing limit and cannot borrow more books.");
        }

        return AccountMapper.mapToAccountDTO(account);
    }

    public boolean checkAccountLoan(Integer id) {
        Integer result = loanRepo.canBorrowBook(id);
        return result != null && result == 1;
    }

}
