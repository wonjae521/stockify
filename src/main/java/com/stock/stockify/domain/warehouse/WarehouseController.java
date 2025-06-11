package com.stock.stockify.domain.warehouse;

import com.stock.stockify.domain.user.User;
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

    // 창고 생성
    @PostMapping
    public ResponseEntity<WarehouseResponse> createWarehouse(@RequestBody CreateWarehouseRequest request,
                                                             @AuthenticationPrincipal User user) {
        Warehouse created = warehouseService.createWarehouse(request, user);
        return ResponseEntity.ok(WarehouseResponse.from(created));
    }

    // 창고 조회
    @GetMapping
    public ResponseEntity<List<WarehouseResponse>> getWarehouses(@AuthenticationPrincipal User user) {
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
        Warehouse updated = warehouseService.updateWarehouse(warehouseId, request, user);
        return ResponseEntity.ok(WarehouseResponse.from(updated));
    }

    // 창고 삭제
    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long warehouseId,
                                                @AuthenticationPrincipal User user) {
        warehouseService.deleteWarehouse(warehouseId, user);
        return ResponseEntity.noContent().build();
    }
}
