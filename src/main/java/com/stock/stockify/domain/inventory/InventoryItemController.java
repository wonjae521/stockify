package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.permission.Permission;
import com.stock.stockify.domain.permission.PermissionRepository;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.user.UserService;
import com.stock.stockify.global.auth.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// 재고 등록, 조회, 삭제 기능 제공
@RestController
@RequestMapping("/api/inventories") // URL: /api/inventories
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;
    private final PermissionChecker permissionChecker;
    private final PermissionRepository permissionRepository;
    private final UserService userService;

    // 재고 등록
    @PostMapping("/warehouses/{warehouseId}")
    public ResponseEntity<String> createInventoryItem(@PathVariable Long warehouseId,
                                                      @RequestBody InventoryItemRequest request,
                                                      @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getUserFromUserDetails(userDetails);
        // 권한 확인
        Permission permission = permissionRepository.findByName("INVENTORY_WRITE")
                .orElseThrow(() -> new RuntimeException("해당 권한이 존재하지 않습니다."));
        permissionChecker.checkAccessToWarehouse(user, warehouseId, permission);

        // 재고 등록
        inventoryItemService.createInventoryItem(warehouseId, request);
        return ResponseEntity.ok("재고 등록 성공");
    }

    // 재고 전체 조회
    @GetMapping("/warehouses/{warehouseId}/items")
    public ResponseEntity<List<InventoryItemResponse>> getItems(@PathVariable Long warehouseId,
                                                                @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getUserFromUserDetails(userDetails);
        // 권한 확인
        Permission permission = permissionRepository.findByName("INVENTORY_READ")
                .orElseThrow(() -> new RuntimeException("해당 권한이 존재하지 않습니다."));
        permissionChecker.checkAccessToWarehouse(user, warehouseId, permission); // 단순 접근 권한만 확인 (읽기니까 권한 플래그 체크는 안함)

        return ResponseEntity.ok(inventoryItemService.getItemsByWarehouse(warehouseId));
    }

    // 재고 수정
    @PutMapping("/warehouses/{warehouseId}/items/{itemId}")
    public ResponseEntity<String> updateItem(@PathVariable Long warehouseId,
                                             @PathVariable Long itemId,
                                             @RequestBody InventoryItemRequest request,
                                             @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getUserFromUserDetails(userDetails);
        // 수정 권한 확인
        Permission permission = permissionRepository.findByName("INVENTORY_WRITE")
                .orElseThrow(() -> new RuntimeException("해당 권한이 존재하지 않습니다."));
        permissionChecker.checkAccessToWarehouse(user, warehouseId, permission);

        inventoryItemService.updateItem(warehouseId, itemId, request);
        return ResponseEntity.ok("재고 수정 완료");
    }

    // 재고 삭제
    @DeleteMapping("/warehouses/{warehouseId}/items/{itemId}")
    public ResponseEntity<String> deleteItem(@PathVariable Long warehouseId,
                                             @PathVariable Long itemId,
                                             @AuthenticationPrincipal UserDetails userDetails) {

        User user = userService.getUserFromUserDetails(userDetails);
        // 삭제 권한 확인
        Permission permission = permissionRepository.findByName("INVENTORY_DELETE")
                .orElseThrow(() -> new RuntimeException("해당 권한이 존재하지 않습니다."));
        permissionChecker.checkAccessToWarehouse(user, warehouseId, permission);

        inventoryItemService.deleteItem(warehouseId, itemId);
        return ResponseEntity.ok("재고 삭제 완료");
    }
}
