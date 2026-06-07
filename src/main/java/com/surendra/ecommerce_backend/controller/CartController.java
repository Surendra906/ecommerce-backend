package com.surendra.ecommerce_backend.controller;

import com.surendra.ecommerce_backend.dto.request.CartItemRequest;
import com.surendra.ecommerce_backend.dto.response.ApiResponse;
import com.surendra.ecommerce_backend.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<?>> addItem(@Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Cart item added successfully")
                .data(cartService.addItem(request))
                .build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Cart fetched successfully")
                .data(cartService.getCart(userId))
                .build());
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<?>> updateItem(@PathVariable Long itemId,
                                                     @RequestParam Long userId,
                                                     @RequestParam Integer quantity) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Cart item updated successfully")
                .data(cartService.updateItem(userId, itemId, quantity))
                .build());
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<ApiResponse<?>> removeItem(@PathVariable Long itemId,
                                                     @RequestParam Long userId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Cart item removed successfully")
                .data(cartService.removeItem(userId, itemId))
                .build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<?>> clearCart(@PathVariable Long userId) {
        cartService.emptyCart(userId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Cart cleared successfully")
                .data(null)
                .build());
    }
}
