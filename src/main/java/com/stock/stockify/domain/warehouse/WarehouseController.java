package com.stock.stockify.domain.warehouse;

import com.stock.stockify.domain.permission.Permission;
import com.stock.stockify.domain.permission.PermissionRepository;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.global.auth.PermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final PermissionRepository permissionRepository;
    private final PermissionChecker permissionChecker;

    // 창고 생성
    @PostMapping
    public ResponseEntity<WarehouseResponse> createWarehouse(@RequestBody CreateWarehouseRequest request,
                                                             @AuthenticationPrincipal User user) {

        Permission permission = permissionRepository.findByName("WAREHOUSE_MANAGE")
                .orElseThrow(() -> new RuntimeException("권한이 존재하지 않습니다."));

        permissionChecker.checkAccessToWarehouse(user, null, permission);

        Warehouse created = warehouseService.createWarehouse(request, user);
        return ResponseEntity.ok(WarehouseResponse.from(created));
    }

    // 창고 조회
    @GetMapping
    public ResponseEntity<List<WarehouseResponse>> getWarehouses(@AuthenticationPrincipal User user) {

        Permission permission = permissionRepository.findByName("WAREHOUSE_VIEW")
                .orElseThrow(() -> new RuntimeException("권한이 존재하지 않습니다."));

        permissionChecker.checkAccessToWarehouse(user, null, permission);

        List<WarehouseResponse> warehouses = warehouseService.getWarehouses(user).stream()
                .map(WarehouseResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(warehouses);
    }

    // 창고 수정
    @PutMapping("/{warehouseId}")
    public ResponseEntity<WarehouseResponse> updateWarehouse(@PathVariable Long warehouseId,
                                                             @RequestBody CreateWarehouseRequest request,
                                                             @AuthenticationPrincipal User user) {

        Permission permission = permissionRepository.findByName("WAREHOUSE_MANAGE")
                .orElseThrow(() -> new RuntimeException("권한이 존재하지 않습니다."));

        permissionChecker.checkAccessToWarehouse(user, null, permission);

        Warehouse updated = warehouseService.updateWarehouse(warehouseId, request, user);
        return ResponseEntity.ok(WarehouseResponse.from(updated));
    }

    // 창고 삭제
    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long warehouseId,
                                                @AuthenticationPrincipal User user) {

        Permission permission = permissionRepository.findByName("WAREHOUSE_MANAGE")
                .orElseThrow(() -> new RuntimeException("권한이 존재하지 않습니다."));

        permissionChecker.checkAccessToWarehouse(user, null, permission);

        warehouseService.deleteWarehouse(warehouseId, user);
        return ResponseEntity.noContent().build();
    }
}
