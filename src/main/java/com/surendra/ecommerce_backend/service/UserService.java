package com.surendra.ecommerce_backend.service;

import com.surendra.ecommerce_backend.dto.request.UserRequest;
import com.surendra.ecommerce_backend.dto.response.UserResponse;
import com.surendra.ecommerce_backend.entity.User;
import com.surendra.ecommerce_backend.exception.BadRequestException;
import com.surendra.ecommerce_backend.exception.ResourceNotFoundException;
import com.surendra.ecommerce_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("User with email already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .build();

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    public UserResponse getUser(Long id) {
        return mapToResponse(findUserById(id));
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
