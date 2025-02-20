package com.university.librarymanagementsystem.service.impl.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.university.librarymanagementsystem.entity.user.Account;
import com.university.librarymanagementsystem.repository.user.AccountRepository;

public class AuthenticationImpl implements UserDetailsService {

    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
        // Account account = new Account();

        return accountRepository.findByUserID(user_id).orElseThrow();
    }

}
