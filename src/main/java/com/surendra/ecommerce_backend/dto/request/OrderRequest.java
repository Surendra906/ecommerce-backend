package com.surendra.ecommerce_backend.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long cartId;
}
