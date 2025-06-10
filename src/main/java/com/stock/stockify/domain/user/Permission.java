package com.stock.stockify.domain.user;

public enum Permission {
    INVENTORY_READ, // 재고 생성, 수정 / ADMIN, SUBADMIN
    INVENTORY_WRITE, // 재고 조회 / ADMIN, SUBADMIN, STAFF
    INVENTORY_DELETE, // 재고 삭제 / ADMIN, SUBADMIN
    ORDER_MANAGE, // 주문 관리(생성, 수정, 조회, 삭제) / ADMIN, SUBADMIN
    REPORT_MANAGE, // 리포트 관리(생성, 수정, 삭제) / ADMIN, SUBADMIN
    REPORT_VIEW, // 리포트 조회 / ADMIN, SUBADMIN, STAFF
    TAG_MANAGE, // 태그 관리(생성, 수정, 조회, 삭제) / ADMIN, SUBADMIN
    NOTIFICATION_MANAGE, // 알림 관리(생성, 수정, 삭제) / ADMIN, SUBADMIN
    NOTIFICATION_VIEW, // 알림 조회 / ADMIN, SUBADMIN, STAFF
    USER_MANAGE, // 사용자 관리(생성, 수정, 조회, 삭제) / ADMIN
    STORAGE_RETRIEVAL_MANAGE // 입출고 기록 관리(생성, 수정, 조회, 삭제) / ADMIN, SUBADMIN
}