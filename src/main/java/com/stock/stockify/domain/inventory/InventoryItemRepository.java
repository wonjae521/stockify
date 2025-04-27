package com.stock.stockify.domain.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    // 추가적인 쿼리가 필요하면 여기에 작성할 수 있어
}
