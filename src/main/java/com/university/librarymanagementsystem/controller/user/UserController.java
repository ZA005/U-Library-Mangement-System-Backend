package com.university.librarymanagementsystem.controller.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.user.UserDTO;
import com.university.librarymanagementsystem.service.user.OTPService;
import com.university.librarymanagementsystem.service.user.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping
public class UserController {

    private UserService service;

    private OTPService otp;

    @GetMapping("/verify/{id}")
    public ResponseEntity<UserDTO> fetchUserById(@PathVariable("id") String user_id) {
        try {
            UserDTO user = service.fetchUserById(user_id);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 if user not found
            }

            String otpCode = otp.generateOTP();
            otp.storeOTP(user.getEmailAdd(), otpCode);
            otp.sendOTPEmail(user.getEmailAdd(), otpCode);

            // Return the user with a 200 OK status
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            // Return an error response if something goes wrong
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
