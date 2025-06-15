package com.stock.stockify.domain.permission;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 서버 시작 시 "ADMIN" 역할을 가진 Role에 대해
 * 권한이 비어 있으면 전체 권한을 자동 부여
 */
@Component
@RequiredArgsConstructor
public class UserRoleInitializer {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;

    @PostConstruct
    public void initializeAdminRoles() {
        // 1. "ADMIN" 이름을 가진 모든 Role 조회
        List<Role> adminRoles = roleRepository.findByName("ADMIN");

        // 2. 전체 권한 목록 조회
        List<Permission> allPermissions = permissionRepository.findAll();

        for (Role role : adminRoles) {
            for (Permission permission : allPermissions) {
                boolean exists = rolePermissionRepository.existsByRoleAndPermission(role, permission);
                if (!exists) {
                    rolePermissionRepository.save(
                            RolePermission.builder()
                                    .role(role)
                                    .permission(permission)
                                    .build()
                    );
                }
            }
        }
    }
}
