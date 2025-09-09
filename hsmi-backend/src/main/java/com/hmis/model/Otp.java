// package com.hmis.model;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// @Entity
// public class Otp {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     private String email;

//     private String otpCode;

//     private LocalDateTime expiryTime;

//     private boolean verified = false;

//     // Getters and Setters
//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }

//     public String getEmail() { return email; }
//     public void setEmail(String email) { this.email = email; }

//     public String getOtpCode() { return otpCode; }
//     public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

//     public LocalDateTime getExpiryTime() { return expiryTime; }
//     public void setExpiryTime(LocalDateTime expiryTime) { this.expiryTime = expiryTime; }

//     public boolean isVerified() { return verified; }
//     public void setVerified(boolean verified) { this.verified = verified; }
// }
