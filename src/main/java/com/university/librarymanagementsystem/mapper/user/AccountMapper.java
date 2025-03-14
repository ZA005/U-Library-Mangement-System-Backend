package com.university.librarymanagementsystem.mapper.user;

import org.springframework.stereotype.Component;

import com.university.librarymanagementsystem.dto.user.AccountDTO;
import com.university.librarymanagementsystem.entity.user.Account;
import com.university.librarymanagementsystem.entity.user.User;

@Component
public class AccountMapper {
    public static AccountDTO mapToAccountDTO(Account account) {
        return new AccountDTO(
                account.getAccount_id(),
                account.getUsers().getId(),
                account.getPassword(),
                account.getRole());
    }

    public static Account mapToAccount(AccountDTO accountDTO) {
        User user = new User();

        user.setId(accountDTO.getUser_id());

        return new Account(
                accountDTO.getAccount_id(),
                user,
                accountDTO.getPassword(),
                accountDTO.getRole());
    }
}
