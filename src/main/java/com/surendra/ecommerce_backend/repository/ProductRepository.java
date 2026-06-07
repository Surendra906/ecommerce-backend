package com.surendra.ecommerce_backend.repository;

import com.surendra.ecommerce_backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryContainingIgnoreCase(String category);
    Page<Product> findByCategoryContainingIgnoreCase(String category, Pageable pageable);
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    Page<Product> findByCategoryContainingIgnoreCaseAndPriceBetween(String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}
