package com.stock.stockify.domain.permission;

import lombok.Getter;

@Getter
public class UserRoleRequestDto {
    private Long userId;
    private Long warehouseId;
    private Long roleId;
}
