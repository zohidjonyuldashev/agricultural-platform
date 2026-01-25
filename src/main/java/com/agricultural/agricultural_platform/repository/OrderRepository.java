package com.agricultural.agricultural_platform.repository;

import com.agricultural.agricultural_platform.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(Order.OrderStatus status);

    long countByStatus(Order.OrderStatus status);

    long countByCreatedAtAfter(LocalDateTime dateTime);
}
