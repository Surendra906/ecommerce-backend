package com.surendra.ecommerce_backend.service;

import com.surendra.ecommerce_backend.dto.request.PaymentRequest;
import com.surendra.ecommerce_backend.dto.response.PaymentResponse;
import com.surendra.ecommerce_backend.entity.Order;
import com.surendra.ecommerce_backend.entity.OrderStatus;
import com.surendra.ecommerce_backend.entity.Payment;
import com.surendra.ecommerce_backend.entity.PaymentStatus;
import com.surendra.ecommerce_backend.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        Order order = orderService.findOrderById(request.getOrderId());

        Payment payment = Payment.builder()
                .order(order)
                .amount(request.getAmount())
                .status(PaymentStatus.INITIATED)
                .transactionReference(UUID.randomUUID().toString())
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        boolean success = ThreadLocalRandom.current().nextBoolean();

        PaymentStatus finalStatus = success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
        savedPayment.setStatus(finalStatus);

        if (success) {
            order.setStatus(OrderStatus.CONFIRMED);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }

        savedPayment = paymentRepository.save(savedPayment);

        return PaymentResponse.builder()
                .transactionId(savedPayment.getId())
                .orderId(order.getId())
                .amount(savedPayment.getAmount())
                .status(savedPayment.getStatus().name())
                .transactionReference(savedPayment.getTransactionReference())
                .createdAt(savedPayment.getCreatedAt())
                .build();
    }
}
