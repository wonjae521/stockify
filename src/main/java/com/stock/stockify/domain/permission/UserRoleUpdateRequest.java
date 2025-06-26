package com.stock.stockify.domain.permission;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleUpdateRequest {
    @NotNull
    private Long userId;

    @NotNull
    private Long warehouseId;

    @NotNull(message = "역할은 필수입니다.")
    private Long roleId;
}
