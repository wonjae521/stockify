package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    // 소유자 기준 전체 조회
    List<InventoryItem> findAllByOwner(User owner);

    // 소유자 기준 단일 조회
    Optional<InventoryItem> findByIdAndOwner(Long id, User owner);

    // 사용자가 소유한 특정 이름의 재고 조회
    Optional<InventoryItem> findByNameAndOwner(String name, User owner);

    /**
    // 중복 재고 판단 (동일 이름 + 단가 + 단위 + 창고 + 소유자)
    Optional<InventoryItem> findByNameAndPriceAndUnitAndWarehouseIdAndOwner(
            String name, Double price, String unit, Long warehouseId, User owner
    );*/

    Optional<InventoryItem> findByWarehouseIdAndOwner(Long warehouseId, User owner);

    long countByOwner(User owner);




}
