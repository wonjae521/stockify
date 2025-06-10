package com.stock.stockify.global.auth;

import com.stock.stockify.domain.user.Permission;
import com.stock.stockify.domain.user.RoleType;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.warehouse.UserWarehouseRole;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Objects;

// 사용자 권한 검사하는 컴포넌트 클래스, 창고 일치 여부, 역할 기반 권한 검사 수행
@Service
public class PermissionChecker {

    // 특정 창고에 대한 권한 보유하고 있는지 검사, 없다면 AccessDeniedException 발생
    public void checkAccessToWarehouse(User user, Long warehouseId, Permission permission) { // user: 검사 대상 사용자, warehouseId: 접근 대상 창고 ID, permission: 요구되는 권한
        if (user.getRoleType() == RoleType.ADMIN) return;

        UserWarehouseRole role = user.getUserWarehouseRoles().stream()
                .filter(r -> Objects.equals(r.getWarehouse().getId(), warehouseId))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("창고 접근 권한이 없습니다."));

        if (!role.getRoleType().hasPermission(permission)) {
            throw new AccessDeniedException("요청 권한이 부족합니다: " + permission);
        }

        if (!user.getRoleType().hasPermission(permission)) {
            throw new AccessDeniedException("요청 권한이 부족합니다: " + permission);
        }
    }

    // 글로벌 권한 검사(ex.관리자 페이지 접근 등)
    public void checkGlobalAccess(User user, Permission permission) {
        if (!user.getRoleType().hasPermission(permission)) {
            throw new AccessDeniedException("전역 권한이 부족합니다: " + permission);
        }
    }
}
