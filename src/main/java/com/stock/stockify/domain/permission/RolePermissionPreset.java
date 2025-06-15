package com.stock.stockify.domain.permission;

import java.util.List;
import java.util.Map;

/**
 * 각 역할(RoleName)에 대해 사전 정의된 권한 코드 목록을 제공하는 유틸리티
 */
public class RolePermissionPreset {

    private static final Map<String, List<String>> rolePermissionMap = Map.of(
            "SUBADMIN", List.of(
                    "INVENTORY_READ", "INVENTORY_WRITE", "INVENTORY_DELETE", "STORAGE_RETRIEVAL_MANAGE",
                    "ORDER_MANAGE", "REPORT_VIEW", "REPORT_MANAGE", "TAG_MANAGE",
                    "NOTIFICATION_VIEW", "NOTIFICATION_MANAGE", "WAREHOUSE_VIEW"
            ),
            "STAFF", List.of(
                    "INVENTORY_READ", "INVENTORY_WRITE",
                    "NOTIFICATION_VIEW"
            )
    );

    /**
     * 주어진 역할에 대한 권한 코드 목록 반환
     * ADMIN은 null 반환 → 전체 권한 부여 처리 대상
     */
    public static List<String> getPermissionCodesForRole(String roleName) {
        return rolePermissionMap.get(roleName.toUpperCase()); // key 대소문자 무시
    }
}
