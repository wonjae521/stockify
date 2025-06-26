package com.stock.stockify.domain.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {
    private String username;
    private String email;
}
