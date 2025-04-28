package com.stock.stockify.domain.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

// InventoryStatusLog 엔티티에 대한 JPA 레포지토리, 기본 CRUD 메서드 제공
public interface InventoryStatusLogRepository extends JpaRepository<InventoryStatusLog, Long> {
}
