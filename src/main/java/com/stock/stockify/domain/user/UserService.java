package com.stock.stockify.domain.user;

import com.stock.stockify.domain.warehouse.Warehouse;
import com.stock.stockify.domain.warehouse.WarehouseRepository;
import com.stock.stockify.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

// 사용자(User) 관련 비즈니스 로직 처리 서비스
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WarehouseRepository warehouseRepository;
    private final JwtUtil jwtUtil;
    private final EmailVerificationService emailVerificationService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    // 회원가입
    public void registerUser(String username, String password, RoleType role, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .roleType(role)
                .email(email)
                .warehouse(null)
                .build();

        userRepository.save(user);
        emailVerificationService.generateToken(
                user.getId(),
                "127.0.0.1",  // 테스트용 IP 주소 (실제 서비스에서는 request.getRemoteAddr() 등으로 대체)
                "EMAIL_VERIFICATION",
                15  // 유효 시간 (분)
        );

       /** // 기본 권한 부여 (예: STAFF에게 INVENTORY_VIEW, ORDER_VIEW 등)
        List<String> defaultPermissions = switch (role) {
            case ADMIN -> List.of("USER_MANAGE", "INVENTORY_MANAGE", "ORDER_MANAGE", "REPORT_GENERATE");
            case SUBADMIN -> List.of("INVENTORY_MANAGE");
            case STAFF -> List.of("INVENTORY_VIEW");
        };

        List<Permission> permissionEntities = permissionRepository.findByNameIn(defaultPermissions);
        for (Permission p : permissionEntities) {
            userPermissionRepository.save(new UserPermission(user, p));
        }*/
    }

    // 로그인
    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRoleType().name());

        return new LoginResponse(token, user.getUsername(), user.getRoleType().name(), user.isEmailVerified());
    }

    // 사용자 정보 찾기(사용자ID)
    public Long getUserIdFromUserDetails(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."))
                .getId();
    }

    // 사용자 정보 찾기(전체)
    public User getUserFromUserDetails(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    // 사용자 정보 찾기(로그인x, 사용자ID, 이메일 사용)
    public Long getUserIdByUsernameAndEmail(String username, String email) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        if (!user.getEmail().equals(email)) {
            throw new IllegalArgumentException("입력한 이메일이 사용자 정보와 일치하지 않습니다.");
        }
        return user.getId();
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

    // 이메일 인증 토큰을 이용한 비밀번호 재설정
    @Transactional
    public void resetPasswordWithToken(String tokenStr, String newPassword) {
        EmailVerificationToken token = emailVerificationTokenRepository.findByToken(tokenStr)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (token.isVerified()) {
            throw new IllegalStateException("이미 사용된 토큰입니다.");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        token.setVerified(true);
        token.setVerifiedAt(LocalDateTime.now());

        userRepository.save(user);
    }

}
