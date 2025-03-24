package com.university.librarymanagementsystem.service.impl.user;

import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.user.AccountDTO;
import com.university.librarymanagementsystem.entity.user.Account;
import com.university.librarymanagementsystem.mapper.user.AccountMapper;
import com.university.librarymanagementsystem.repository.user.AccountRepository;
import com.university.librarymanagementsystem.service.user.AccountService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountImpl implements AccountService {
    private AccountRepository accountRepo;

    @Override
    public AccountDTO fetchAccountByID(String user_id) {
        Account account = accountRepo.findByUserID(user_id).orElse(null);
        if (account == null) {
            return null;
        }
        System.out.println("ACCOUNT: " + account.getAccount_id());

        return AccountMapper.mapToAccountDTO(account);
    }
}
