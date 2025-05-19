package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.permission.WarehousePermissionRequest;
import com.stock.stockify.domain.user.UserRole;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.warehouse.Warehouse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    // 창고 생성
    @PostMapping("/warehouses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Warehouse> createWarehouse(@RequestParam String name,
                                                     @RequestParam(required = false) String description) {
        Warehouse created = adminUserService.createWarehouse(name, description);
        return ResponseEntity.ok(created);
    }

    // 사용자 등록 (직원/보조 관리자) + 권한 설정
    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> registerUserWithWarehouse(@RequestParam String username,
                                                          @RequestParam String password,
                                                          @RequestParam String email,
                                                          @RequestParam UserRole role,
                                                          @RequestBody @Valid List<WarehousePermissionRequest> warehousePermissions) {
        User createdUser = adminUserService.registerUserWithWarehouse(username, password, email, role, warehousePermissions);
        return ResponseEntity.ok(createdUser);
    }
}
