package com.stock.stockify.domain.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// 기본 CRUD 메소드 제공
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findByWarehouseId(Long warehouseId);
    Optional<InventoryItem> findByIdAndWarehouseId(Long id, Long warehouseId);

}
