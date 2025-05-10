package com.university.librarymanagementsystem.service.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class OTPService {

    private final EmailService emailService;

    public OTPService(EmailService emailService) {
        this.emailService = emailService;
    }

    Random random = new Random();
    private Map<String, String> otpStorage = new HashMap<>();

    // Generates OTP and stores it in the map with the associated email or phone
    // number
    public String generateOTP() {
        int otp = 100000 + random.nextInt(900000); // Generates a 6-digit OTP
        return String.valueOf(otp);
    }

    public void storeOTP(String key, String otp) {
        otpStorage.put(key, otp); // Store OTP associated with a key (email or phone)
    }

    public boolean verifyOTP(String key, String otp) {
        String storedOtp = otpStorage.get(key); // Retrieve the OTP associated with the key (email)
        if (storedOtp == null) {
            return false; // No OTP stored for this email
        }
        return otp.equals(storedOtp);
    }

    public void sendOTPEmail(String email, String otp, boolean isActivation) {
        emailService.sendOTPEmail(email, otp, isActivation);
    }

}
