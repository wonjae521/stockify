package com.stock.stockify.global.security;

import com.stock.stockify.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

// Spring Security에서 인증된 사용자 정보를 담는 클래스
public class CustomUserDetails implements UserDetails {

    private final User user;

    // 생성자에서 User 엔티티를 받아 저장
    public CustomUserDetails(User user) {
        this.user = user;
    }

    // 나중에 AuditorAwareImpl에서 사용
    public User getUser() {
        return user;
    }

    // 사용자 권한 정보 반환 (여기선 생략했지만 ROLE 설정하려면 여기에 추가)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 또는 List.of(new SimpleGrantedAuthority("ROLE_" + user.getRoleType().name()))
    }

    // 비밀번호 반환
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 사용자 이름(ID) 반환
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 아래는 계정 상태 설정 (true면 사용 가능)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
