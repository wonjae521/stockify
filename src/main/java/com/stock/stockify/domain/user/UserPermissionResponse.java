package com.stock.stockify.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserPermissionResponse {
    private Long userId;
    private List<String> permissions;
}
