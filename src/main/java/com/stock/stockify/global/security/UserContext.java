package com.stock.stockify.global.security;

import com.stock.stockify.domain.user.User;
import com.stock.stockify.global.exception.UnauthorizedException;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserContext {

    public static User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.getClass().getName().equals("com.stock.stockify.domain.user.User")) {
            return (User) principal;
        }

        // 또는 instanceof CustomUserDetails 검사도 병행
        if (principal instanceof CustomUserDetails custom) {
            return custom.getUser();
        }

        throw new UnauthorizedException("인증된 사용자를 찾을 수 없습니다.");

    }
}
