package com.stock.stockify.global.auth;

import com.stock.stockify.domain.permission.Permission;
import com.stock.stockify.domain.permission.PermissionRepository;
import com.stock.stockify.domain.permission.RolePermissionRepository;
import com.stock.stockify.domain.permission.UserRole;
import com.stock.stockify.domain.permission.UserRoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 사용자 권한을 검사하는 유틸리티 클래스
 * - 전역 권한 검사 또는 창고별 접근 권한 검사 지원
 */
@Component
@RequiredArgsConstructor
public class PermissionChecker {

    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;

    /**
     * ✅ 창고와 무관하게 사용자가 전체 권한 중 하나라도 갖고 있는지 검사
     */
    @Transactional
    public void check(Long userId, String permissionCode) {
        Permission permission = permissionRepository.findByName(permissionCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 권한 코드: " + permissionCode));

        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);

        boolean hasPermission = userRoles.stream()
                .anyMatch(userRole ->
                        rolePermissionRepository.existsByRoleAndPermission(userRole.getRole(), permission)
                );

        if (!hasPermission) {
            throw new AccessDeniedException("권한이 없습니다: " + permissionCode);
        }
    }

    /**
     * ✅ 특정 창고에 대해 사용자가 해당 권한을 갖고 있는지 검사
     */
    @Transactional
    public void checkAccessToWarehouse(Long userId, Long warehouseId, String permissionCode) {
        Permission permission = permissionRepository.findByName(permissionCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 권한 코드: " + permissionCode));

        List<UserRole> userRoles = userRoleRepository.findByUserIdAndWarehouseId(userId, warehouseId);

        boolean hasPermission = userRoles.stream()
                .anyMatch(userRole ->
                        rolePermissionRepository.existsByRoleAndPermission(userRole.getRole(), permission)
                );

        if (!hasPermission) {
            throw new AccessDeniedException("해당 창고에 대한 권한이 없습니다: " + permissionCode);
        }
    }
}
