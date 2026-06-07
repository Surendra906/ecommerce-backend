package com.surendra.ecommerce_backend.controller;

import com.surendra.ecommerce_backend.dto.request.UserRequest;
import com.surendra.ecommerce_backend.dto.response.ApiResponse;
import com.surendra.ecommerce_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createUser(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("User created successfully")
                .data(userService.createUser(request))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("User fetched successfully")
                .data(userService.getUser(id))
                .build());
    }
}
