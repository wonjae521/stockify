package com.stock.stockify.domain.user;

import com.stock.stockify.domain.permission.*;
import com.stock.stockify.domain.warehouse.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    // 창고 생성
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

    // 사용자 등록 + 창고 권한 연결
    @Transactional
    public UserResponseDto registerUserWithWarehouse(String username, String password, String email, Long roleId, List<Long> warehouseIds) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        // ADMIN 생성 방지
        String roleName = resolveRoleName(roleId);
        if ("ADMIN".equalsIgnoreCase(roleName)) {
            throw new AccessDeniedException("ADMIN 계정은 관리자만 생성할 수 있습니다.");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .emailVerified(false)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        // Role을 먼저 조회
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다."));

        // 창고별 UserRole 및 RolePermission 연결
        List<Permission> permissions = getPermissionsForRole(roleName);
        for (Long warehouseId : warehouseIds) {
            Warehouse warehouse = warehouseRepository.findById(warehouseId)
                    .orElseThrow(() -> new RuntimeException("창고를 찾을 수 없습니다."));

            // UserRole로 연결
            UserRole userRole = UserRole.builder()
                    .user(user)
                    .warehouse(warehouse)
                    .role(role)
                    .build();

            userRoleRepository.save(userRole);

            for (Permission permission : permissions) {
                RolePermission rolePermission = RolePermission.builder()
                        .role(userRole.getRole())
                        .permission(permission)
                        .build();
                rolePermissionRepository.save(rolePermission);
            }
        }

        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), List.of(roleName),
                warehouseIds);
    }

    private String resolveRoleName(Long roleId) {
        return switch (roleId.intValue()) {
            case 2 -> "SUBADMIN";
            case 3 -> "STAFF";
            default -> throw new IllegalArgumentException("지원되지 않는 역할입니다.");
        };
    }

    private List<Permission> getPermissionsForRole(String roleName) {
        return switch (roleName.toUpperCase()) {
            case "SUBADMIN" -> permissionRepository.findByNameIn(List.of(
                    "INVENTORY_READ", "INVENTORY_WRITE",
                    "ORDER_MANAGE", "REPORT_VIEW",
                    "NOTIFICATION_VIEW", "WAREHOUSE_VIEW"
            ));
            case "STAFF" -> permissionRepository.findByNameIn(List.of(
                    "INVENTORY_READ", "INVENTORY_WRITE",
                    "NOTIFICATION_VIEW"
            ));
            default -> throw new IllegalArgumentException("정의되지 않은 역할: " + roleName);
        };
    }
}
