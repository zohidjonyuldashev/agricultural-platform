package com.agricultural.agricultural_platform.repository;

import com.agricultural.agricultural_platform.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    Optional<Price> findByProductName(String productName);

    List<Price> findByCategory(String category);

    @Query("SELECT AVG(p.currentPrice) FROM Price p")
    BigDecimal findAveragePrice();
}
