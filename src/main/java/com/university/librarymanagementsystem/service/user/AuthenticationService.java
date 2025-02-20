package com.university.librarymanagementsystem.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.university.librarymanagementsystem.dto.user.RequestResponse;
import com.university.librarymanagementsystem.entity.user.Account;
import com.university.librarymanagementsystem.entity.user.User;
import com.university.librarymanagementsystem.repository.user.AccountRepository;
import com.university.librarymanagementsystem.repository.user.UserRepository;

public class AuthenticationService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private PasswordEncoder passEncoder;

    public RequestResponse register(RequestResponse registrationRequest) {
        RequestResponse response = new RequestResponse();

        try {
            // Find or create the user
            User user = userRepository.findById(registrationRequest.getUser_id())
                    .orElseThrow(
                            () -> new RuntimeException("User not found with ID: " + registrationRequest.getUser_id()));

            // Create new Account entity
            Account account = new Account();
            account.setUsers(user);
            account.setPassword(passEncoder.encode(registrationRequest.getPassword()));
            account.setRole(registrationRequest.getRole());

            // Save the account in DB
            account = accountRepository.save(account);

            // Generate JWT Token
            String token = jwtUtils.generateToken(account);

            // Populate response
            response.setStatusCode(200);
            response.setMessage("User registered successfully");
            response.setUser_id(user.getId());
            response.setToken(token);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Registration failed: " + e.getMessage());
        }

        return response;
    }

}
