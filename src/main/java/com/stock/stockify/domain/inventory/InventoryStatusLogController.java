package com.stock.stockify.domain.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class InventoryStatusLogController {

    private final InventoryStatusLogService inventoryStatusLogService;

    // 입출고 기록 등록
    @PostMapping
    public ResponseEntity<String> createLog(@RequestBody InventoryStatusLogRequest request) {
        inventoryStatusLogService.createLog(request);
        return ResponseEntity.ok("입출고 기록 저장 완료");
    }

    // 입출고 기록 전체 조회
    @GetMapping
    public ResponseEntity<List<InventoryStatusLog>> getAllLogs() {
        List<InventoryStatusLog> logs = inventoryStatusLogService.getAllLogs();
        return ResponseEntity.ok(logs);
    }
}
