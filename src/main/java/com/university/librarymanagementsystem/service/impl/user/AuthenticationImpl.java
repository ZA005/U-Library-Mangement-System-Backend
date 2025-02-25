package com.university.librarymanagementsystem.service.impl.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.university.librarymanagementsystem.repository.user.AccountRepository;

@Service
public class AuthenticationImpl implements UserDetailsService {

    private AccountRepository accountRepository;

    public AuthenticationImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
        return accountRepository.findByUserID(user_id).orElseThrow();
    }

}
