package com.stock.stockify.domain.permission;

// import java.util.Set;

/**
public enum RoleType {
    ADMIN, SUBADMIN, STAFF
}
*/





/**
// 사용자 역할 유형을 정의하는 enum, 각 역할에 따라 부여된 Permission 목록 내장함
public enum RoleType {

    // 최상위 관리자 - 모든 권한 부여
    ADMIN(Set.of(Permission.values())),

    // 부관리자 - 제한된 관리자 권한 부여
    SUBADMIN(Set.of(
            Permission.INVENTORY_READ,
            Permission.INVENTORY_WRITE,
            Permission.ORDER_MANAGE,
            Permission.REPORT_VIEW
    )),

    // 직원 - 최소 권한 보유
    STAFF(Set.of(
            Permission.INVENTORY_READ,
            Permission.REPORT_VIEW
    ));

    private final Set<Permission> permissions;

    RoleType(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    // 해당 역할이 특정 권한 보유하고 있는지 확인
    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    // 모든 권한 반환 (테스트 및 디버깅용)
    public Set<Permission> getPermissions() {
        return permissions;
    }
}
 */