package com.university.librarymanagementsystem.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.user.RequestResponse;
import com.university.librarymanagementsystem.service.user.AuthenticationService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class AuthController {

    private AuthenticationService auth;

    @PostMapping("/auth/register")
    public ResponseEntity<RequestResponse> register(@RequestBody RequestResponse accountInformation) {
        RequestResponse response = auth.register(accountInformation);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("auth/login")
    public ResponseEntity<RequestResponse> login(@RequestBody RequestResponse accountInformation) {
        RequestResponse response = auth.login(accountInformation);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @PostMapping("auth/refresh-token")
    public ResponseEntity<RequestResponse> refreshToken(@RequestBody RequestResponse token) {
        RequestResponse response = auth.refreshToken(token);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }

    @GetMapping("auth/verify/{user_id}")
    public ResponseEntity<RequestResponse> isActivated(@PathVariable String user_id) {
        RequestResponse response = auth.isActivated(user_id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
