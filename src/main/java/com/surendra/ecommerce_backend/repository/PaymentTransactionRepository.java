package com.surendra.ecommerce_backend.repository;

import com.surendra.ecommerce_backend.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
}
