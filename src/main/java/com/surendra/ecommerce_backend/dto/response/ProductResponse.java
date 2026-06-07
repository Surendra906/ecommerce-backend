package com.surendra.ecommerce_backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponse {
    private Long productId;
    private String productName;
    private String description;
    private String category;
    private BigDecimal price;
    private Integer stockQuantity;
    private LocalDateTime createdAt;
}
