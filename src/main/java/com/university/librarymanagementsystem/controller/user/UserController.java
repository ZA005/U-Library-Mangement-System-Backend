package com.university.librarymanagementsystem.controller.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.librarymanagementsystem.dto.user.OtpVerificationDTO;
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
    public ResponseEntity<UserDTO> sendOTP(@PathVariable("id") String user_id) {
        try {
            UserDTO user = service.fetchUserById(user_id);
            System.out.println("USER:" + user);
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

    @PostMapping("/verify/otp")
    public ResponseEntity<Map<String, String>> confirmOTP(@RequestBody OtpVerificationDTO user) {
        Map<String, String> response = new HashMap<>();

        if (user == null || user.getEmailAdd() == null || user.getOtp() == null) {
            response.put("success", "false");
            response.put("message", "Invalid request payload");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 Bad Request
        }

        boolean isVerified = otp.verifyOTP(user.getEmailAdd(), user.getOtp());

        if (!isVerified) {
            response.put("success", "false");
            response.put("message", "Invalid or expired OTP!");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }

        response.put("success", "true");
        response.put("message", "OTP verified successfully!");
        return new ResponseEntity<>(response, HttpStatus.OK); // 200 OK
    }

    @GetMapping("/public/user/{id}")
    public ResponseEntity<UserDTO> fetchUser(@PathVariable("id") String user_id) {
        UserDTO user = service.fetchUserById(user_id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
