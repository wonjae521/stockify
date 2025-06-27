package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.user.UserService;
import com.stock.stockify.global.auth.PermissionChecker;
import com.stock.stockify.global.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;
    private final PermissionChecker permissionChecker;
    private final UserService userService;

    /**
     * 재고 등록
     */
    @PostMapping("/warehouses/{warehouseId}")
    public ResponseEntity<InventoryItemResponse> create(@PathVariable Long warehouseId,
                                                        @RequestBody InventoryItemRequest request) {
        User user = UserContext.getCurrentUser();
        permissionChecker.checkAccessToWarehouse(user.getId(), warehouseId, "INVENTORY_WRITE");

        InventoryItem created = inventoryItemService.createInventoryItem(warehouseId, request);
        return ResponseEntity.ok(InventoryItemResponse.from(created));
    }

    /**
     * 재고 전체 조회
     */
    @GetMapping("/warehouses/{warehouseId}")
    public ResponseEntity<List<InventoryItemResponse>> getAll(@PathVariable Long warehouseId) {
        User user = UserContext.getCurrentUser();
        permissionChecker.checkAccessToWarehouse(user.getId(), warehouseId, "INVENTORY_VIEW");

        return ResponseEntity.ok(inventoryItemService.getItemsByWarehouse(warehouseId));
    }

    /**
     * 재고 단일 조회
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<InventoryItemResponse> getOne(@PathVariable Long itemId) {
        return ResponseEntity.ok(inventoryItemService.getItem(itemId));
    }

    /**
     * 재고 수정
     */
    @PutMapping("/warehouses/{warehouseId}/items/{itemId}")
    public ResponseEntity<Void> update(@PathVariable Long warehouseId,
                                       @PathVariable Long itemId,
                                       @RequestBody InventoryItemRequest request) {
        User user = UserContext.getCurrentUser();
        permissionChecker.checkAccessToWarehouse(user.getId(), warehouseId, "INVENTORY_WRITE");

        inventoryItemService.updateInventoryItem(warehouseId, itemId, request);
        return ResponseEntity.ok().build();
    }

    /**
     * 재고 삭제 (Soft Delete)
     */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> delete(@PathVariable Long itemId) {
        User user = UserContext.getCurrentUser();
        inventoryItemService.deleteInventoryItem(itemId);
        return ResponseEntity.noContent().build();
    }
}
