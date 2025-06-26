package com.stock.stockify.domain.user;

import com.stock.stockify.domain.warehouse.Warehouse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    // 사용자 등록 (직원/보조 관리자) + 창고 연결
    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> registerUserWithWarehouse(@RequestBody @Valid AdminUserRegisterRequest request,
                                                                     @AuthenticationPrincipal User adminUser) {
        UserResponseDto createdUser = adminUserService.registerUserWithWarehouse(
                request.getUsername(),
                request.getPassword(),
                request.getEmail(),
                request.getRoleId(),
                request.getWarehouseIds(),
                adminUser
        );
        return ResponseEntity.ok(createdUser);
    }
}
