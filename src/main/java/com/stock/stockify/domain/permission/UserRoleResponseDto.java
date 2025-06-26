package com.stock.stockify.domain.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserRoleResponseDto {
    private Long warehouseId;
    private String warehouseName;
    private String role;
    private List<String> permissions;
}
