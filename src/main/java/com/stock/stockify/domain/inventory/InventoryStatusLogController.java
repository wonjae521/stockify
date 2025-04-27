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

    @PostMapping
    public ResponseEntity<InventoryStatusLog> createLog(@RequestParam Long itemId,
                                                        @RequestParam String status,
                                                        @RequestParam int quantity) {
        InventoryStatusLog log = inventoryStatusLogService.createLog(itemId, status, quantity);
        return ResponseEntity.ok(log);
    }

    @GetMapping
    public ResponseEntity<List<InventoryStatusLog>> getAllLogs() {
        List<InventoryStatusLog> logs = inventoryStatusLogService.getAllLogs();
        return ResponseEntity.ok(logs);
    }
}
