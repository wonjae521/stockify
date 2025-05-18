package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.inventory.InventoryStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface InventoryStatusLogRepository extends JpaRepository<InventoryStatusLog, Long> {

    @Query("SELECT COALESCE(SUM(l.quantity), 0) FROM InventoryStatusLog l " +
            "WHERE l.action = 'INBOUND' AND l.warehouseId = :warehouseId " +
            "AND l.createdAt BETWEEN :start AND :end")
    int sumInboundQuantity(@Param("warehouseId") Long warehouseId,
                           @Param("start") LocalDate start,
                           @Param("end") LocalDate end);

    @Query("SELECT COALESCE(SUM(l.quantity), 0) FROM InventoryStatusLog l " +
            "WHERE l.action = 'OUTBOUND' AND l.warehouseId = :warehouseId " +
            "AND l.createdAt BETWEEN :start AND :end")
    int sumOutboundQuantity(@Param("warehouseId") Long warehouseId,
                            @Param("start") LocalDate start,
                            @Param("end") LocalDate end);

}
