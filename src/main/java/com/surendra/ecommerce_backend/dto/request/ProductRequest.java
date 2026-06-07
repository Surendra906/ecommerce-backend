package com.surendra.ecommerce_backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank
    private String productName;

    private String description;

    private String category;

    @NotNull
    @Min(value = 0, message = "Price must be positive")
    private BigDecimal price;

    @NotNull
    @Min(value = 0, message = "Stock quantity must be non-negative")
    private Integer stockQuantity;
}
