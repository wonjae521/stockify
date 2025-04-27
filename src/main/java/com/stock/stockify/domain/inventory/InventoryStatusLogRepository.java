package com.stock.stockify.domain.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryStatusLogRepository extends JpaRepository<InventoryStatusLog, Long> {
}
