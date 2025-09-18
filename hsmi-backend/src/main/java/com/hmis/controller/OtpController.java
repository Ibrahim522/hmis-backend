package com.hmis.controller;

import com.hmis.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/otp")
// @CrossOrigin(origins = "http://localhost:3000")  
public class OtpController {

    private final OtpService otpService;

    @Autowired
    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/send-otp")   
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        System.out.println("Received email for OTP: " + email);

        if (email == null || email.isEmpty()) {
            System.out.println("Email is empty or null");
            return ResponseEntity.badRequest().body("Email is required");
        }

        try {
            otpService.sendOtpEmail(email);
            System.out.println("OTP sent successfully");
            return ResponseEntity.ok("OTP sent");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send OTP");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (email == null || otp == null || email.isEmpty() || otp.isEmpty()) {
            return ResponseEntity.badRequest().body("Email and OTP are required");
        }

        boolean verified = otpService.verifyOtp(email, otp);
        if (verified) {
            return ResponseEntity.ok("OTP verified");
        } else {
            return ResponseEntity.status(400).body("Invalid OTP");
        }
    }
}

