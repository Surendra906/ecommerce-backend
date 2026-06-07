package com.surendra.ecommerce_backend.service;

import com.surendra.ecommerce_backend.dto.request.OrderRequest;
import com.surendra.ecommerce_backend.dto.request.OrderStatusRequest;
import com.surendra.ecommerce_backend.dto.response.OrderItemResponse;
import com.surendra.ecommerce_backend.dto.response.OrderResponse;
import com.surendra.ecommerce_backend.entity.*;
import com.surendra.ecommerce_backend.exception.BadRequestException;
import com.surendra.ecommerce_backend.exception.ResourceNotFoundException;
import com.surendra.ecommerce_backend.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CartService cartService;
    private final ProductService productService;

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        User user = userService.findUserById(request.getUserId());
        Cart cart = cartService.getCartEntityForOrder(request.getCartId(), user);

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty and cannot be converted into an order");
        }

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Product product = productService.findProductById(cartItem.getProduct().getProductId());
            if (cartItem.getQuantity() > product.getStockQuantity()) {
                throw new BadRequestException("Insufficient stock for product " + product.getProductName());
            }
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(cartItem.getUnitPrice())
                    .build();
            order.addItem(orderItem);
            total = total.add(orderItem.getTotalPrice());
        }

        order.setTotalAmount(total);
        orderRepository.save(order);
        productService.saveProducts(order);
        cartService.clearCart(cart);
        return mapToResponse(order);
    }

    public OrderResponse getOrder(Long orderId) {
        return mapToResponse(findOrderById(orderId));
    }

    public List<OrderResponse> listOrdersByUser(Long userId) {
        User user = userService.findUserById(userId);
        return orderRepository.findByUser(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> listAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusRequest request) {
        Order order = findOrderById(orderId);
        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid order status: " + request.getStatus());
        }

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("Cannot update status for completed or cancelled orders");
        }

        order.setStatus(newStatus);
        return mapToResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = findOrderById(orderId);
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Order is already cancelled");
        }
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("Delivered orders cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
        });

        productService.saveProducts(order);
        return mapToResponse(orderRepository.save(order));
    }

    protected Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getProductId())
                        .productName(item.getProduct().getProductName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }
}
