package com.surendra.ecommerce_backend.service;

import com.surendra.ecommerce_backend.dto.request.CartItemRequest;
import com.surendra.ecommerce_backend.dto.response.CartItemResponse;
import com.surendra.ecommerce_backend.dto.response.CartResponse;
import com.surendra.ecommerce_backend.entity.Cart;
import com.surendra.ecommerce_backend.entity.CartItem;
import com.surendra.ecommerce_backend.entity.Product;
import com.surendra.ecommerce_backend.entity.User;
import com.surendra.ecommerce_backend.exception.BadRequestException;
import com.surendra.ecommerce_backend.exception.ResourceNotFoundException;
import com.surendra.ecommerce_backend.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductService productService;

    @Transactional
    public CartResponse addItem(CartItemRequest request) {
        User user = userService.findUserById(request.getUserId());
        Product product = productService.findProductById(request.getProductId());

        if (request.getQuantity() > product.getStockQuantity()) {
            throw new BadRequestException("Quantity exceeds available stock");
        }

        Cart cart = cartRepository.findByUser(user).orElse(Cart.builder().user(user).build());
        CartItem item = cart.getItems().stream()
                .filter(existing -> existing.getProduct().getProductId().equals(product.getProductId()))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = CartItem.builder()
                    .product(product)
                    .quantity(request.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();
            cart.addItem(item);
        } else {
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.setUnitPrice(product.getPrice());
        }

        Cart savedCart = cartRepository.save(cart);
        return mapToResponse(savedCart);
    }

    public CartResponse getCart(Long userId) {
        User user = userService.findUserById(userId);
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id " + userId));
        return mapToResponse(cart);
    }

    @Transactional
    public CartResponse updateItem(Long userId, Long itemId, Integer quantity) {
        if (quantity < 1) {
            throw new BadRequestException("Quantity must be at least 1");
        }

        User user = userService.findUserById(userId);
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id " + userId));

        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id " + itemId));

        if (quantity > item.getProduct().getStockQuantity()) {
            throw new BadRequestException("Quantity exceeds available stock");
        }

        item.setQuantity(quantity);
        Cart savedCart = cartRepository.save(cart);
        return mapToResponse(savedCart);
    }

    @Transactional
    public CartResponse removeItem(Long userId, Long itemId) {
        User user = userService.findUserById(userId);
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id " + userId));

        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id " + itemId));

        cart.removeItem(item);
        Cart savedCart = cartRepository.save(cart);
        return mapToResponse(savedCart);
    }

    @Transactional
    public void emptyCart(Long userId) {
        User user = userService.findUserById(userId);
        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }

    @Transactional
    public Cart getCartEntityForOrder(Long cartId, User user) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with id " + cartId));
        if (!cart.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Cart does not belong to the specified user");
        }
        return cart;
    }

    @Transactional
    public void clearCart(Cart cart) {
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    private CartResponse mapToResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(item -> CartItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getProductId())
                        .productName(item.getProduct().getProductName())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .totalPrice(item.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(CartItemResponse::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(items)
                .total(total)
                .build();
    }
}
