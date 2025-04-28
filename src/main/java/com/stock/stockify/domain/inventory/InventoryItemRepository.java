package com.stock.stockify.domain.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

// 기본 CRUD 메소드 제공
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
}
