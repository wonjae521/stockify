package com.stock.stockify.global.auth;

import com.stock.stockify.domain.permission.Permission;
import com.stock.stockify.domain.permission.RolePermissionRepository;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.warehouse.UserWarehouseRole;
import com.stock.stockify.domain.warehouse.UserWarehouseRoleRepository;
import com.stock.stockify.global.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionChecker {

    private final UserWarehouseRoleRepository userWarehouseRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    /**
     * 사용자가 해당 창고에서 주어진 권한을 가지고 있는지 검사한다.
     * @param user 현재 로그인한 사용자
     * @param warehouseId 검사 대상 창고 ID
     * @param permission 검사할 권한 객체
     */
    public void checkAccessToWarehouse(User user, Long warehouseId, Permission permission) {
        // 사용자가 이 창고에서 어떤 역할을 가지고 있는지 확인
        UserWarehouseRole userWarehouseRole = userWarehouseRoleRepository
                .findByUserAndWarehouseId(user.getId(), warehouseId)
                .orElseThrow(() -> new ForbiddenException("해당 창고에 접근 권한이 없습니다."));

        // 그 역할이 해당 권한을 포함하는지 확인
        boolean hasPermission = rolePermissionRepository
                .existsByRoleAndPermission(userWarehouseRole.getRole(), permission);

        // 없으면 예외 발생
        if (!hasPermission) {
            throw new ForbiddenException("해당 작업을 수행할 권한이 없습니다.");
        }
    }
}























/**
import com.stock.stockify.domain.permission.Permission;
import com.stock.stockify.domain.role.RolePermissionRepository;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.user.UserWarehouseRole;
import com.stock.stockify.domain.user.UserWarehouseRoleRepository;
import com.stock.stockify.domain.warehouse.Warehouse;
import com.stock.stockify.global.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionChecker {

    private final UserWarehouseRoleRepository userWarehouseRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;


     * 사용자가 해당 창고에서 주어진 권한을 가지고 있는지 검사한다.
     * @param user 현재 로그인한 사용자
     * @param warehouseId 검사 대상 창고 ID
     * @param permission 검사할 권한 객체

    public void checkAccessToWarehouse(User user, Long warehouseId, Permission permission) {
        // 1. 사용자가 이 창고에서 어떤 역할을 가지고 있는지 확인
        UserWarehouseRole userWarehouseRole = userWarehouseRoleRepository
                .findByUserAndWarehouseId(user.getId(), warehouseId)
                .orElseThrow(() -> new ForbiddenException("해당 창고에 접근 권한이 없습니다."));

        // 2. 그 역할이 해당 권한을 포함하는지 확인
        boolean hasPermission = rolePermissionRepository
                .existsByRoleAndPermission(userWarehouseRole.getRole(), permission);

        // 3. 없으면 예외 발생
        if (!hasPermission) {
            throw new ForbiddenException("해당 작업을 수행할 권한이 없습니다.");
        }
    }
}
*/