package com.stock.stockify.domain.permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSummaryResponseDto {
    private Long id;
    private String username;
    private String email;
}
