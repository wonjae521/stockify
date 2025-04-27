package com.stock.stockify.domain.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories") // 📌 URL: /api/inventories
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    // 🔥 재고 등록
    @PostMapping
    public ResponseEntity<String> createInventoryItem(@RequestBody InventoryItemRequest request) {
        inventoryItemService.createInventoryItem(request);
        return ResponseEntity.ok("재고 등록 성공");
    }

    // 🔥 재고 전체 조회
    @GetMapping
    public ResponseEntity<List<InventoryItem>> getAllInventoryItems() {
        List<InventoryItem> items = inventoryItemService.getAllInventoryItems();
        return ResponseEntity.ok(items);
    }

    // 🔥 재고 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInventoryItem(@PathVariable Long id) {
        inventoryItemService.deleteInventoryItem(id);
        return ResponseEntity.ok("재고 삭제 성공");
    }
}
