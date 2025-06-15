package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.permission.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PermissionResponseDto {
    private String name;
    private String description;

    public static PermissionResponseDto from(Permission permission) {
        return new PermissionResponseDto(permission.getName(), permission.getDescription());
    }
}
