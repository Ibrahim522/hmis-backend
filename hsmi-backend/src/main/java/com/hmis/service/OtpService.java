package com.hmis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @Autowired
    private JavaMailSender mailSender;

    // Store OTPs temporarily in memory (for demo only)
    private Map<String, String> otpStorage = new ConcurrentHashMap<>();

    public void sendOtpEmail(String email) {
        String otp = generateOtp();
        otpStorage.put(email, otp);

        // Prepare the email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp);

        // Send the email
        mailSender.send(message);

        System.out.println("OTP sent to " + email + ": " + otp);
    }

    public boolean verifyOtp(String email, String otp) {
        String savedOtp = otpStorage.get(email);
        return otp.equals(savedOtp);
    }

    private String generateOtp() {
        int otp = (int)(Math.random() * 900000) + 100000; // Generates a random 6-digit OTP
        return String.valueOf(otp);
    }
}
