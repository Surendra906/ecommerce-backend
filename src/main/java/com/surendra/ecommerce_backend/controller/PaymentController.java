package com.surendra.ecommerce_backend.controller;

import com.surendra.ecommerce_backend.dto.request.PaymentRequest;
import com.surendra.ecommerce_backend.dto.response.ApiResponse;
import com.surendra.ecommerce_backend.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> processPayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Payment processed successfully")
                .data(paymentService.processPayment(request))
                .build());
    }
}
