package com.surendra.ecommerce_backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotNull
    private Long orderId;

    @NotNull
    @Min(value = 0, message = "Amount must be non-negative")
    private BigDecimal amount;

    private String paymentMethod;
}
