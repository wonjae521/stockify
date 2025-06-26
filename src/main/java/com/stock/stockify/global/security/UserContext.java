package com.stock.stockify.global.security;

import com.stock.stockify.domain.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserContext {

    public static User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getUser();
        }
        throw new IllegalStateException("인증된 사용자를 찾을 수 없습니다.");
    }
}
