package com.stock.stockify.domain.user;

import com.stock.stockify.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 사용자(User) 관련 비즈니스 로직 처리 서비스
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PermissionRepository permissionRepository;
    private final UserPermissionRepository userPermissionRepository;

    // 회원가입
    public void registerUser(String username, String password, UserRole role) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .role(role)
                .build();

        userRepository.save(user);

        // 기본 권한 부여 (예: STAFF에게 INVENTORY_VIEW, ORDER_VIEW 등)
        List<String> defaultPermissions = switch (role) {
            case ADMIN -> List.of("USER_MANAGE", "INVENTORY_MANAGE", "ORDER_MANAGE", "REPORT_GENERATE");
            case SUBADMIN -> List.of("INVENTORY_MANAGE");
            case STAFF -> List.of("INVENTORY_VIEW");
        };

        List<Permission> permissionEntities = permissionRepository.findByNameIn(defaultPermissions);
        for (Permission p : permissionEntities) {
            userPermissionRepository.save(new UserPermission(user, p));
        }
    }
    // 로그인
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        if (!user.isEmailVerified()) {
            throw new RuntimeException("이메일 인증이 완료되지 않았습니다.");
        }

        // 로그인 시 권한도 JWT에 포함
        List<String> permissions = user.getUserPermissions().stream()
                .map(up -> up.getPermission().getName())
                .toList();

        return jwtUtil.generateToken(user.getUsername(), user.getRole().name(), permissions);
    }
    // 사용자 정보 찾기
    public Long getUserIdFromUserDetails(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."))
                .getId();
    } 
    // 사용자 아이디 수정
    @Transactional
    public void updateUsername(Long userId, String newUsername) {
        if (userRepository.existsByUsername(newUsername)) { // 존재 여부 확인 메서드로 수정
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        user.setUsername(newUsername); // 엔티티에서 username 필드가 변경 가능해야 함
    }
    // 사용자 비밀번호 수정
    @Transactional
    public void updatePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
    }

}
