package com.surendra.ecommerce_backend.service;

import com.surendra.ecommerce_backend.dto.request.ProductRequest;
import com.surendra.ecommerce_backend.dto.response.ProductResponse;
import com.surendra.ecommerce_backend.entity.Order;
import com.surendra.ecommerce_backend.entity.OrderItem;
import com.surendra.ecommerce_backend.entity.Product;
import com.surendra.ecommerce_backend.exception.ResourceNotFoundException;
import com.surendra.ecommerce_backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .productName(request.getProductName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .build();

        return mapToResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long productId, ProductRequest request) {
        Product product = findProductById(productId);
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        return mapToResponse(productRepository.save(product));
    }

    public ProductResponse getProduct(Long productId) {
        return mapToResponse(findProductById(productId));
    }

    public Page<ProductResponse> listProducts(Pageable pageable, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        if (category != null && minPrice != null && maxPrice != null) {
            return productRepository.findByCategoryContainingIgnoreCaseAndPriceBetween(category, minPrice, maxPrice, pageable)
                    .map(this::mapToResponse);
        }

        if (category != null) {
            return productRepository.findByCategoryContainingIgnoreCase(category, pageable)
                    .map(this::mapToResponse);
        }

        if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceBetween(minPrice, maxPrice, pageable)
                    .map(this::mapToResponse);
        }

        return productRepository.findAll(pageable).map(this::mapToResponse);
    }

    public List<ProductResponse> listProductsByCategory(String category) {
        return productRepository.findByCategoryContainingIgnoreCase(category).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteProduct(Long productId) {
        productRepository.delete(findProductById(productId));
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    public void saveProducts(Order order) {
        List<Product> products = order.getItems().stream()
                .map(OrderItem::getProduct)
                .collect(Collectors.toList());
        productRepository.saveAll(products);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
