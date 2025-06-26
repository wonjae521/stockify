package com.stock.stockify.global.security;

import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Spring Security에서 로그인할 때 호출되는 사용자 조회 서비스
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 생성자 주입 (의존성 주입)
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // username을 기준으로 DB에서 사용자 정보를 찾아 반환
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자 이름으로 User 엔티티 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        // User 엔티티를 CustomUserDetails로 감싸서 반환 (Spring Security가 이 객체를 사용)
        return new CustomUserDetails(user);
    }
}
