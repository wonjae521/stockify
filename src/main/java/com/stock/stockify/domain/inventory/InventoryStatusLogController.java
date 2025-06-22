package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.inventory.InventoryStatusLog;
import com.stock.stockify.domain.inventory.InventoryStatusLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 입출고 기록 관련 API 요청을 처리하는 컨트롤러
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class InventoryStatusLogController {

    private final InventoryStatusLogService inventoryStatusLogService;

    // 전체 로그 조회 (본인의 ADMIN 데이터만)
    @GetMapping
    public ResponseEntity<List<InventoryStatusLog>> getAllLogs() {
        List<InventoryStatusLog> logs = inventoryStatusLogService.getMyLogs();
        return ResponseEntity.ok(logs);
    }

    // 창고별 로그 조회
    @GetMapping("/warehouses/{warehouseId}")
    public ResponseEntity<List<InventoryStatusLog>> getLogsByWarehouse(@PathVariable Long warehouseId) {
        List<InventoryStatusLog> logs = inventoryStatusLogService.getLogsByWarehouse(warehouseId);
        return ResponseEntity.ok(logs);
    }
}
