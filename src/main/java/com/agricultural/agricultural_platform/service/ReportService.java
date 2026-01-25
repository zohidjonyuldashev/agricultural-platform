package com.agricultural.agricultural_platform.service;

import com.agricultural.agricultural_platform.entity.Order;
import com.agricultural.agricultural_platform.repository.OrderRepository;
import com.agricultural.agricultural_platform.repository.PriceRepository;
import com.agricultural.agricultural_platform.repository.ProductRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PriceRepository priceRepository;
    private final MeterRegistry meterRegistry;
    private Counter reportGenerationCounter;

    @PostConstruct
    public void initMetrics() {
        reportGenerationCounter = Counter.builder("daily_report_generation_count")
                .description("Number of daily reports generated")
                .register(meterRegistry);
    }

    public Map<String, Object> generateDailyReport() {
        reportGenerationCounter.increment();

        Map<String, Object> report = new HashMap<>();

        // Product statistics
        report.put("totalProducts", productRepository.count());
        report.put("availableProducts", productRepository.countByAvailableTrue());

        // Order statistics
        report.put("totalOrders", orderRepository.count());
        report.put("pendingOrders", orderRepository.countByStatus(Order.OrderStatus.PENDING));
        report.put("completedOrders", orderRepository.countByStatus(Order.OrderStatus.DELIVERED));
        report.put("ordersToday", orderRepository.countByCreatedAtAfter(
                LocalDateTime.now().minusDays(1)));

        // Price statistics
        BigDecimal avgPrice = priceRepository.findAveragePrice();
        report.put("averagePrice", avgPrice != null ? avgPrice : BigDecimal.ZERO);
        report.put("totalPriceEntries", priceRepository.count());

        // System health
        report.put("reportGeneratedAt", LocalDateTime.now());
        report.put("systemStatus", "HEALTHY");

        return report;
    }
}
