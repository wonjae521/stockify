package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.permission.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Optional;

@Getter
public class RoleResponseDto {
    private Long id;
    private String name;
    private List<String> permissions;

    public RoleResponseDto(Long id, String name, List<String> permissions) {
        this.id = id;
        this.name = name;
        this.permissions = permissions;
    }

    public static RoleResponseDto from(Role role) {
        List<String> permissionNames = Optional.ofNullable(role.getRolePermissions())
                .orElse(List.of()) // null 방지
                .stream()
                .map(rp -> rp.getPermission().getName())
                .toList();

        return new RoleResponseDto(role.getId(), role.getName(), permissionNames);
    }
}
