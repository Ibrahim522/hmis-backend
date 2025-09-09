package com.hmis.controller;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    private final String stripeSecretKey = "sk_test_51S2syLCjoVPMv76inPdAPowDQqfTPEZmlvcwltbFuW45YqpDHEStXEZMWYNfxC47nP86sdJYeW4L85ugEHefAC9b004Dg92RfQ";

    @PostMapping("/create-payment-intent")
    public Map<String, Object> createPaymentIntent(@RequestBody Map<String, Object> data) throws Exception {
        Stripe.apiKey = stripeSecretKey;


        Long amountInPkr = 1499L; 
        Long amountInPaisa = amountInPkr * 100;
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amountInPaisa);
        params.put("currency", "pkr");  
        params.put("automatic_payment_methods", Map.of("enabled", true));

        PaymentIntent intent = PaymentIntent.create(params);

        Map<String, Object> response = new HashMap<>();
        response.put("clientSecret", intent.getClientSecret());
        response.put("amount", amountInPaisa);  // send amount to frontend for display
        return response;
    }
}
