package com.stock.stockify.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByCreatedAtBetweenAndItem_WarehouseId(LocalDateTime start, LocalDateTime end, Long warehouseId);
}
