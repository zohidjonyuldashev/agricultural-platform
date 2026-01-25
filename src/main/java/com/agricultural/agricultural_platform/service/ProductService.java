package com.agricultural.agricultural_platform.service;

import com.agricultural.agricultural_platform.entity.Product;
import com.agricultural.agricultural_platform.repository.ProductRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final MeterRegistry meterRegistry;
    private Counter productsCreatedCounter;

    @PostConstruct
    public void initMetrics() {
        productsCreatedCounter = Counter.builder("products_created_total")
                .description("Total number of products created")
                .register(meterRegistry);

        Gauge.builder("active_products_count", productRepository, ProductRepository::countByAvailableTrue)
                .description("Number of currently available products")
                .register(meterRegistry);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findByAvailableTrue();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        Product saved = productRepository.save(product);
        productsCreatedCounter.increment();
        return saved;
    }

    public Optional<Product> updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setCategory(productDetails.getCategory());
                    product.setQuantity(productDetails.getQuantity());
                    product.setUnit(productDetails.getUnit());
                    product.setFarmerName(productDetails.getFarmerName());
                    product.setAvailable(productDetails.getAvailable());
                    return productRepository.save(product);
                });
    }

    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
