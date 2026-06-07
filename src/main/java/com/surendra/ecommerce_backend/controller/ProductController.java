package com.surendra.ecommerce_backend.controller;

import com.surendra.ecommerce_backend.dto.request.ProductRequest;
import com.surendra.ecommerce_backend.dto.response.ApiResponse;
import com.surendra.ecommerce_backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Product created successfully")
                .data(productService.createProduct(request))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateProduct(@PathVariable Long id,
                                                        @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Product updated successfully")
                .data(productService.updateProduct(id, request))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Product fetched successfully")
                .data(productService.getProduct(id))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> listProducts(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @PageableDefault(sort = "productName") Pageable pageable) {

        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Products fetched successfully")
                .data(productService.listProducts(pageable, category, minPrice, maxPrice))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Product deleted successfully")
                .data(null)
                .build());
    }
}
