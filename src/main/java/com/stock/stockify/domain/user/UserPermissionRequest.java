package com.stock.stockify.domain.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 유저 권한 DTO
@Getter
@Setter
public class UserPermissionRequest {
    private List<String> permissions; // ex: ["INVENTORY_MANAGE", "ORDER_MANAGE"]
}

