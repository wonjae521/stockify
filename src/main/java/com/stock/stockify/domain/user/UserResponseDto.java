package com.stock.stockify.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

// ADMIN이 SUBADMIN, STAFF 회원가입 하기 위함.
@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private RoleType role;
    private List<Long> warehouseIds;
}
