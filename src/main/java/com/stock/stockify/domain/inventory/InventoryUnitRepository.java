package com.stock.stockify.domain.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryUnitRepository extends JpaRepository<InventoryUnit, Long> {

    // 특정 바코드 ID가 존재하는지 확인
    boolean existsByBarcodeId(String barcodeId);

    // 바코드로 단일 유닛 조회
    Optional<InventoryUnit> findByBarcodeId(String barcodeId);
}
