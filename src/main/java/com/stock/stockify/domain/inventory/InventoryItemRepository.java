package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// 기본 CRUD 메소드 제공
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    List<InventoryItem> findByWarehouseId(Long warehouseId);
    Optional<InventoryItem> findByIdAndWarehouseId(Long id, Long warehouseId);

    // 사용자별 재고 수 카운트 / 이메일 미인증자용
    @Query("SELECT COUNT(i) FROM InventoryItem i WHERE i.createdBy = :user")
    long countByCreatedBy(@Param("user") User user);

}
