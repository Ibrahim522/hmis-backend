package com.hmis.controller;

import com.stripe.model.PaymentIntent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${stripeSecretKey:sk_test_mockKey}")
    private String stripeSecretKey;

    @Test
    public void testCreatePaymentIntent_Success() throws Exception {
        // Prepare mock PaymentIntent
        PaymentIntent mockPaymentIntent = Mockito.mock(PaymentIntent.class);
        Mockito.when(mockPaymentIntent.getClientSecret()).thenReturn("mock_client_secret_123");

        // Mock static PaymentIntent.create()
        try (MockedStatic<PaymentIntent> paymentIntentMockedStatic = mockStatic(PaymentIntent.class)) {
            paymentIntentMockedStatic.when(() -> PaymentIntent.create(Mockito.anyMap()))
                .thenReturn(mockPaymentIntent);

            Map<String, Object> requestBody = new HashMap<>();
            // your controller ignores the body, so can send empty JSON
            String jsonRequest = objectMapper.writeValueAsString(requestBody);

            mockMvc.perform(post("/api/payment/create-payment-intent")
                            .contentType("application/json")
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.clientSecret").value("mock_client_secret_123"))
                    .andExpect(jsonPath("$.amount").value(149900L));
        }
    }

    @Test
    public void testCreatePaymentIntent_Failure() throws Exception {
        // Mock static PaymentIntent.create() to throw exception
        try (MockedStatic<PaymentIntent> paymentIntentMockedStatic = mockStatic(PaymentIntent.class)) {
            paymentIntentMockedStatic.when(() -> PaymentIntent.create(Mockito.anyMap()))
                    .thenThrow(new RuntimeException("Stripe API error"));

            Map<String, Object> requestBody = new HashMap<>();
            String jsonRequest = objectMapper.writeValueAsString(requestBody);

            mockMvc.perform(post("/api/payment/create-payment-intent")
                            .contentType("application/json")
                            .content(jsonRequest))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.error").value("Stripe API error"));
        }
    }
}
        