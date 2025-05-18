package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.user.*;
import com.stock.stockify.domain.warehouse.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserWarehouseRoleRepository userWarehouseRoleRepository;
    private final PasswordEncoder passwordEncoder;

    // 1. 창고 생성
    @Transactional
    public Warehouse createWarehouse(String name, String description) {
        if (warehouseRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 창고 이름입니다.");
        }
        return warehouseRepository.save(Warehouse.builder()
                .name(name)
                .description(description)
                .build());
    }

    // 2. 사용자 등록 + 창고 권한 연결
    @Transactional
    public User registerUserWithWarehouse(String username, String password, String email, UserRole role,
                                          List<WarehousePermissionRequest> warehousePermissions) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .emailVerified(false)  // 기본값
                .role(role)
                .build();

        userRepository.save(user);

        for (WarehousePermissionRequest wp : warehousePermissions) {
            Warehouse warehouse = warehouseRepository.findById(wp.getWarehouseId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 창고 ID: " + wp.getWarehouseId()));

            if (userWarehouseRoleRepository.existsByUserIdAndWarehouseId(user.getId(), warehouse.getId())) {
                throw new IllegalArgumentException("이미 매핑된 창고입니다.");
            }

            UserWarehouseRole roleMapping = UserWarehouseRole.builder()
                    .user(user)
                    .warehouse(warehouse)
                    .canManageInventory(wp.isCanManageInventory())
                    .canManageOrders(wp.isCanManageOrders())
                    .canViewReports(wp.isCanViewReports())
                    .build();

            userWarehouseRoleRepository.save(roleMapping);
        }

        return user;
    }
}
