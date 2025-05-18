package com.stock.stockify.global.auth;

import com.stock.stockify.domain.warehouse.UserWarehouseRole;
import com.stock.stockify.domain.warehouse.UserWarehouseRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionChecker {

    private final UserWarehouseRoleRepository roleRepository;

    public void checkInventoryAccess(Long userId, Long warehouseId) {
        UserWarehouseRole role = getRole(userId, warehouseId);
        if (!role.isCanManageInventory()) {
            throw new AccessDeniedException("재고 관리 권한이 없습니다.");
        }
    }

    public void checkOrderAccess(Long userId, Long warehouseId) {
        UserWarehouseRole role = getRole(userId, warehouseId);
        if (!role.isCanManageOrders()) {
            throw new AccessDeniedException("주문 관리 권한이 없습니다.");
        }
    }

    public void checkReportAccess(Long userId, Long warehouseId) {
        UserWarehouseRole role = getRole(userId, warehouseId);
        if (!role.isCanViewReports()) {
            throw new AccessDeniedException("보고서 조회 권한이 없습니다.");
        }
    }

    // 접근 권한만 확인(조회처럼 단순 존재 여부만 확인)
    public void checkAccessToWarehouse(Long userId, Long warehouseId) {
        roleRepository.findByUserIdAndWarehouseId(userId, warehouseId)
                .orElseThrow(() -> new AccessDeniedException("해당 창고에 대한 접근 권한이 없습니다."));
    }

    // 권한 객체 조회(수정/삭제 등에서 권한 속성 확인)
    private UserWarehouseRole getRole(Long userId, Long warehouseId) {
        return roleRepository.findByUserIdAndWarehouseId(userId, warehouseId)
                .orElseThrow(() -> new AccessDeniedException("해당 창고에 대한 접근 권한이 없습니다."));
    }
}
