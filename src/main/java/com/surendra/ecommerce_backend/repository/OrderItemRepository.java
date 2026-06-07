package com.surendra.ecommerce_backend.repository;

import com.surendra.ecommerce_backend.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
