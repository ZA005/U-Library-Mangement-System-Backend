package com.university.librarymanagementsystem.service.impl.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.repository.user.UserRepo;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String uncIdNumber) throws UsernameNotFoundException {
        return userRepo.findBySchoolId(uncIdNumber).orElseThrow();
    }

}
