package com.agricultural.agricultural_platform.service;

import com.agricultural.agricultural_platform.entity.Order;
import com.agricultural.agricultural_platform.repository.OrderRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MeterRegistry meterRegistry;
    private Counter ordersPlacedCounter;
    private Timer orderProcessingTimer;

    @PostConstruct
    public void initMetrics() {
        ordersPlacedCounter = Counter.builder("orders_placed_total")
                .description("Total number of orders placed")
                .register(meterRegistry);

        orderProcessingTimer = Timer.builder("order_processing_duration_seconds")
                .description("Time taken to process orders")
                .register(meterRegistry);

        Gauge.builder("pending_orders_count", orderRepository,
                        repo -> repo.countByStatus(Order.OrderStatus.PENDING))
                .description("Number of pending orders")
                .register(meterRegistry);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Order createOrder(Order order) {
        return orderProcessingTimer.record(() -> {
            Order saved = orderRepository.save(order);
            ordersPlacedCounter.increment();
            return saved;
        });
    }

    public Optional<Order> updateOrderStatus(Long id, Order.OrderStatus status) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(status);
                    return orderRepository.save(order);
                });
    }

    public boolean deleteOrder(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
