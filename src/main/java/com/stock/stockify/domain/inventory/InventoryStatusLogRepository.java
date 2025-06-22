package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.inventory.InventoryStatusLog;
import com.stock.stockify.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InventoryStatusLogRepository extends JpaRepository<InventoryStatusLog, Long> {

    // 입고 수량 합계
    @Query("SELECT COALESCE(SUM(l.quantity), 0) FROM InventoryStatusLog l " +
            "WHERE l.action = 'INBOUND' AND l.warehouseId = :warehouseId " +
            "AND l.createdAt BETWEEN :start AND :end")
    int sumInboundQuantity(@Param("warehouseId") Long warehouseId,
                           @Param("start") LocalDate start,
                           @Param("end") LocalDate end);

    // 출고 수량 합계
    @Query("SELECT COALESCE(SUM(l.quantity), 0) FROM InventoryStatusLog l " +
            "WHERE l.action = 'OUTBOUND' AND l.warehouseId = :warehouseId " +
            "AND l.createdAt BETWEEN :start AND :end")
    int sumOutboundQuantity(@Param("warehouseId") Long warehouseId,
                            @Param("start") LocalDate start,
                            @Param("end") LocalDate end);

    // 관리자 기준 전체 로그 조회
    List<InventoryStatusLog> findByOwner(User owner);

    // 관리자 + 창고 기준 필터링
    List<InventoryStatusLog> findByOwnerAndWarehouseId(User owner, Long warehouseId);
}
