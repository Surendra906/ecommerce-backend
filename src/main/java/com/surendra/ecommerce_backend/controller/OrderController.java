package com.surendra.ecommerce_backend.controller;

import com.surendra.ecommerce_backend.dto.request.OrderRequest;
import com.surendra.ecommerce_backend.dto.request.OrderStatusRequest;
import com.surendra.ecommerce_backend.dto.response.ApiResponse;
import com.surendra.ecommerce_backend.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> placeOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Order placed successfully")
                .data(orderService.placeOrder(request))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> listOrders() {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Orders fetched successfully")
                .data(orderService.listAllOrders())
                .build());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<?>> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Order fetched successfully")
                .data(orderService.getOrder(orderId))
                .build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<?>> getOrdersByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("User orders fetched successfully")
                .data(orderService.listOrdersByUser(userId))
                .build());
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<?>> updateOrderStatus(@PathVariable Long orderId,
                                                            @Valid @RequestBody OrderStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Order status updated successfully")
                .data(orderService.updateOrderStatus(orderId, request))
                .build());
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<?>> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Order cancelled successfully")
                .data(orderService.cancelOrder(orderId))
                .build());
    }
}
