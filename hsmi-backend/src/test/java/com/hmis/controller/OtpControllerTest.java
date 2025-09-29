package com.hmis.controller;

import com.hmis.service.OtpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OtpController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OtpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OtpService otpService;

    @BeforeEach
    public void setup() {
        Mockito.reset(otpService);
    }

    @Test
    public void testSendOtp_Success() throws Exception {
        doNothing().when(otpService).sendOtpEmail(anyString());

        String requestBody = "{\"email\":\"user@example.com\"}";

        mockMvc.perform(post("/otp/send-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("OTP sent"));

        verify(otpService, times(1)).sendOtpEmail("user@example.com");
    }

    @Test
    
    public void testSendOtp_MissingEmail() throws Exception {
        String requestBody = "{}";

        mockMvc.perform(post("/otp/send-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email is required"));

        verify(otpService, never()).sendOtpEmail(anyString());
    }

    @Test
    public void testSendOtp_ServiceThrowsException() throws Exception {
        doThrow(new RuntimeException("Email service error"))
                .when(otpService).sendOtpEmail(anyString());

        String requestBody = "{\"email\":\"user@example.com\"}";

        mockMvc.perform(post("/otp/send-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to send OTP"));

        verify(otpService, times(1)).sendOtpEmail("user@example.com");
    }

    @Test
    public void testVerifyOtp_Success() throws Exception {
        when(otpService.verifyOtp("user@example.com", "123456")).thenReturn(true);

        String requestBody = "{\"email\":\"user@example.com\", \"otp\":\"123456\"}";

        mockMvc.perform(post("/otp/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("OTP verified"));

        verify(otpService, times(1)).verifyOtp("user@example.com", "123456");
    }

    @Test
    public void testVerifyOtp_MissingFields() throws Exception {
        String requestBody = "{\"email\":\"user@example.com\"}";  // missing otp

        mockMvc.perform(post("/otp/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email and OTP are required"));

        verify(otpService, never()).verifyOtp(anyString(), anyString());
    }

    @Test
    public void testVerifyOtp_InvalidOtp() throws Exception {
        when(otpService.verifyOtp("user@example.com", "wrongotp")).thenReturn(false);

        String requestBody = "{\"email\":\"user@example.com\", \"otp\":\"wrongotp\"}";

        mockMvc.perform(post("/otp/verify-otp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid OTP"));

        verify(otpService, times(1)).verifyOtp("user@example.com", "wrongotp");
    }
}
