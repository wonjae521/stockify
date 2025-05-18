package com.stock.stockify.domain.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {
    private String token;
    private String newPassword;
}
