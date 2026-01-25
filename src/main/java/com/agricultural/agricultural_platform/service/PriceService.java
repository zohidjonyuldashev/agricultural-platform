package com.agricultural.agricultural_platform.service;

import com.agricultural.agricultural_platform.entity.Price;
import com.agricultural.agricultural_platform.repository.PriceRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;
    private final MeterRegistry meterRegistry;
    private Counter priceUpdatesCounter;

    @PostConstruct
    public void initMetrics() {
        priceUpdatesCounter = Counter.builder("price_updates_total")
                .description("Total number of price updates")
                .register(meterRegistry);

        Gauge.builder("current_average_price", priceRepository, repo -> {
                    BigDecimal avg = repo.findAveragePrice();
                    return avg != null ? avg.doubleValue() : 0.0;
                })
                .description("Current average price across all products")
                .register(meterRegistry);
    }

    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    public Optional<Price> getPriceById(Long id) {
        return priceRepository.findById(id);
    }

    public Optional<Price> getPriceByProductName(String productName) {
        return priceRepository.findByProductName(productName);
    }

    public Price createPrice(Price price) {
        return priceRepository.save(price);
    }

    public Optional<Price> updatePrice(Long id, BigDecimal newPrice) {
        return priceRepository.findById(id)
                .map(price -> {
                    price.setPreviousPrice(price.getCurrentPrice());
                    price.setCurrentPrice(newPrice);
                    priceUpdatesCounter.increment();
                    return priceRepository.save(price);
                });
    }

    public Optional<Price> rollbackPrice(Long id) {
        return priceRepository.findById(id)
                .filter(price -> price.getPreviousPrice() != null)
                .map(price -> {
                    BigDecimal current = price.getCurrentPrice();
                    price.setCurrentPrice(price.getPreviousPrice());
                    price.setPreviousPrice(current);
                    return priceRepository.save(price);
                });
    }

    public boolean deletePrice(Long id) {
        if (priceRepository.existsById(id)) {
            priceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
