package com.agricultural.agricultural_platform.controller;

import com.agricultural.agricultural_platform.service.ReportService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final MeterRegistry meterRegistry;
    private Counter seasonalTrafficCounter;

    @PostConstruct
    public void initMetrics() {
        seasonalTrafficCounter = Counter.builder("seasonal_traffic_requests_total")
                .description("Total requests during seasonal peaks")
                .register(meterRegistry);
    }

    @GetMapping("/daily")
    @Timed(value = "api.reports.daily", description = "Time to generate daily report")
    public ResponseEntity<Map<String, Object>> getDailyReport() {
        seasonalTrafficCounter.increment();
        return ResponseEntity.ok(reportService.generateDailyReport());
    }

    @GetMapping("/health")
    @Timed(value = "api.reports.health", description = "Time to get health status")
    public ResponseEntity<Map<String, String>> getHealthStatus() {
        seasonalTrafficCounter.increment();
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "application", "Agricultural Platform",
                "version", "1.0.0"
        ));
    }
}