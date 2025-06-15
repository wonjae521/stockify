package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.permission.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoleResponseDto {
    private Long id;
    private String name;

    public static RoleResponseDto from(Role role) {
        return new RoleResponseDto(role.getId(), role.getName());
    }
}
