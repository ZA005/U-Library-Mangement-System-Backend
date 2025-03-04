package com.university.librarymanagementsystem.service.user;

import java.util.HashMap;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.university.librarymanagementsystem.dto.user.RequestResponse;
import com.university.librarymanagementsystem.entity.user.Account;
import com.university.librarymanagementsystem.entity.user.User;
import com.university.librarymanagementsystem.repository.user.AccountRepository;
import com.university.librarymanagementsystem.repository.user.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class AuthenticationService {

    private final AccountRepository accountRepo;
    private final UserRepository userRepo;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(AccountRepository accountRepo, UserRepository userRepo, JWTUtils jwtUtils,
            AuthenticationManager authManager, PasswordEncoder passwordEncoder) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
        this.jwtUtils = jwtUtils;
        this.authManager = authManager;
        this.passwordEncoder = passwordEncoder;
    }

    public RequestResponse isActivated(String user_id) {
        System.out.println("USER ID:" + user_id);
        RequestResponse response = new RequestResponse();

        try {
            // Check if the user is activated
            Integer isActive = accountRepo.existByUserID(user_id);
            if (isActive == 1) {
                response.setStatusCode(409);
                response.setMessage("User is already activated.");
                return response;
            }

            // Check if the user exists
            Boolean isExist = userRepo.existsById(user_id);
            if (!isExist) {
                response.setStatusCode(404);
                response.setMessage("User ID not found");
                return response;
            }

            response.setStatusCode(200);
            response.setMessage("User is not activated.");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error checking activation: " + e.getMessage());
        }

        return response;
    }

    public RequestResponse login(RequestResponse loginRequest) {

        String user_id = loginRequest.getUser_id();
        String password = loginRequest.getPassword();
        RequestResponse response = new RequestResponse();

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user_id, password));
            var account = accountRepo.findByUserID(loginRequest.getUser_id()).orElseThrow();
            var token = jwtUtils.generateToken(account);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), account);

            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(account.getRole());
            response.setRefreshToken(refreshToken);
            response.setUser_id(user_id);
            response.setExpirationTime("6Hrs");
            response.setMessage("Successfully Logged In");

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());

        }
        return response;

    }

    public RequestResponse register(RequestResponse registrationResponse) {
        RequestResponse response = new RequestResponse();
        User user = new User();

        user.setId(registrationResponse.getUser_id());
        try {
            Account account = new Account();
            account.setUsers(user);
            account.setPassword(passwordEncoder.encode(registrationResponse.getPassword()));
            account.setRole(registrationResponse.getRole());

            Account result = accountRepo.save(account);

            if (result.getAccount_id() > 0) {
                response.setAccounts((result));
                response.setMessage("Account save successfully!");
                response.setStatusCode(200);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }

        return response;
    }

    public RequestResponse refreshToken(RequestResponse refreshTokenRequest) {
        RequestResponse response = new RequestResponse();

        try {
            String user_id = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            Account account = accountRepo.findByUserID(user_id).orElseThrow();

            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), account)) {
                var token = jwtUtils.generateToken(account);

                response.setStatusCode(200);
                response.setToken(token);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("6Hrs");
                response.setMessage("Successfully Refresh Token!");
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());

        }
        return response;
    }

    @PostConstruct
    public void checkBeans() {
        System.out.println("AccountRepository Bean Exists: " + (accountRepo != null));
    }

}
