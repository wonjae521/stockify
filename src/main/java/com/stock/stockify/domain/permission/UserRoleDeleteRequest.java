package com.stock.stockify.domain.permission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleDeleteRequest {
    private Long userId;
    private Long warehouseId;
}
