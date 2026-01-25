package com.agricultural.agricultural_platform.controller;

import com.agricultural.agricultural_platform.entity.Price;
import com.agricultural.agricultural_platform.service.PriceService;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @GetMapping
    @Timed(value = "api.prices.getAll", description = "Time to get all prices")
    public ResponseEntity<List<Price>> getAllPrices() {
        return ResponseEntity.ok(priceService.getAllPrices());
    }

    @GetMapping("/{id}")
    @Timed(value = "api.prices.getById", description = "Time to get price by ID")
    public ResponseEntity<Price> getPriceById(@PathVariable Long id) {
        return priceService.getPriceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/product/{productName}")
    @Timed(value = "api.prices.getByProductName", description = "Time to get price by product name")
    public ResponseEntity<Price> getPriceByProductName(@PathVariable String productName) {
        return priceService.getPriceByProductName(productName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Timed(value = "api.prices.create", description = "Time to create price")
    public ResponseEntity<Price> createPrice(@Valid @RequestBody Price price) {
        Price created = priceService.createPrice(price);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    @Timed(value = "api.prices.update", description = "Time to update price")
    public ResponseEntity<Price> updatePrice(@PathVariable Long id,
                                             @RequestParam BigDecimal newPrice) {
        return priceService.updatePrice(id, newPrice)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/rollback")
    @Timed(value = "api.prices.rollback", description = "Time to rollback price")
    public ResponseEntity<Price> rollbackPrice(@PathVariable Long id) {
        return priceService.rollbackPrice(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}")
    @Timed(value = "api.prices.delete", description = "Time to delete price")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        if (priceService.deletePrice(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
