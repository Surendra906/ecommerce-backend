package com.surendra.ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderStatusRequest {

    @NotBlank
    private String status;
}
