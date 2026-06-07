package com.surendra.ecommerce_backend.repository;

import com.surendra.ecommerce_backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
