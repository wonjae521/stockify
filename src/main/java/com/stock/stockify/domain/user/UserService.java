package com.stock.stockify.domain.user;

import com.stock.stockify.domain.permission.*;
import com.stock.stockify.domain.warehouse.UserWarehouseRole;
import com.stock.stockify.domain.warehouse.UserWarehouseRoleRepository;
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
    private final JwtUtil jwtUtil;
    private final EmailVerificationService emailVerificationService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserWarehouseRoleRepository userWarehouseRoleRepository;

    // 회원가입
    public void registerUser(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }
        /**
        // 2. roleId를 통해 roleName 문자열 가져오기 (예: STAFF)
        String roleName = resolveRoleName(roleId); // ❗️새 메서드로 대체
        if (roleName == null) {
            throw new IllegalArgumentException("유효하지 않은 역할입니다.");
        }*/

        // 오직 ADMIN만 허용
        String roleName = "ADMIN";

        // 사용자 생성
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .emailVerified(false)
                // .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        // 이메일 인증 토큰 발급
        emailVerificationService.generateToken(
                user.getId(),
                "127.0.0.1",        // 테스트용 IP 주소 (실제 서비스에서는 request.getRemoteAddr() 등으로 대체)
                "EMAIL_VERIFICATION",        // 유효 시간 (분)
                15
        );

        // ADMIN 역할 생성
        Role adminRole = roleRepository.save(Role.builder()
                .name("ADMIN")
                .admin(user)
                .build());

        // ADMIN 역할에 전체 권한 부여
        List<Permission> allPermissions = permissionRepository.findAll();
        for (Permission permission : allPermissions) {
            RolePermission rolePermission = RolePermission.builder()
                    .role(adminRole)
                    .permission(permission)
                    .build();
            rolePermissionRepository.save(rolePermission);
        }

        // ADMIN 전용 창고 생성
        Warehouse personalWarehouse = warehouseRepository.save(
                Warehouse.builder()
                        .name(username + "_기본창고")
                        .description("ADMIN " + username + " 전용 기본 창고")
                        .admin(user)
                        .build());

        // 해당 창고에 ADMIN 역할 할당
        UserRole userRole = UserRole.builder()
                .user(user)
                .warehouse(personalWarehouse)
                .role(adminRole)
                .build();
        userRoleRepository.save(userRole);

        UserWarehouseRole userWarehouseRole = UserWarehouseRole.builder()
                .user(user)
                .warehouse(personalWarehouse)  // 개인 창고
                .role(adminRole)               // ADMIN 역할
                .build();

        userWarehouseRoleRepository.save(userWarehouseRole);


        /**
         * 기본 권한 부여 (예: STAFF에게 INVENTORY_VIEW, ORDER_VIEW 등)
         * 지금은 창고 ID 1번 기준. 추후 프론트에서 전달받을 수 있음
         */
        /**
        // 기본 창고 할당
        Warehouse defaultWarehouse = warehouseRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("기본 창고 없음"));

        Role role = roleRepository.save(
                Role.builder()
                        .name("ADMIN")
                        .admin(user)
                        .build()
        );

        UserRole userRole = UserRole.builder()
                .user(user)
                .warehouse(defaultWarehouse)
                .role(role)
                .build();

        userRoleRepository.save(userRole);

        // ADMIN -> 전체권한 부여
        List<Permission> permissions = getPermissionsForRole(roleName);
        for (Permission permission : permissions) {
            RolePermission rolePermission = RolePermission.builder()
                    .role(userRole.getRole())
                    .permission(permission)
                    .build();
            rolePermissionRepository.save(rolePermission);
        } */
    }

    // 역할명에 따른 권한 목록 반환
    private List<Permission> getPermissionsForRole(String roleName) {
        List<String> permissionCodes = RolePermissionPreset.getPermissionCodesForRole(roleName);
        if (permissionCodes == null) {
            return permissionRepository.findAll(); // ADMIN 등 전체 권한 부여
        }
        return permissionRepository.findByNameIn(permissionCodes);
    }

       /** // 기본 권한 부여 (예: STAFF에게 INVENTORY_VIEW, ORDER_VIEW 등)
        List<String> defaultPermissions = switch (role) {
            case ADMIN -> List.of("USER_MANAGE", "INVENTORY_MANAGE", "ORDER_MANAGE", "REPORT_GENERATE");
            case SUBADMIN -> List.of("INVENTORY_MANAGE");
            case STAFF -> List.of("INVENTORY_VIEW");
        };

        List<Permission> permissionEntities = permissionRepository.findByNameIn(defaultPermissions);
        for (Permission p : permissionEntities) {
            userPermissionRepository.save(new UserPermission(user, p));
        }
    }*/

    // 로그인
    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        UserRole representativeRole = userRoleRepository.findByUserId(user.getId())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("역할이 지정되지 않은 사용자입니다."));

        String roleName = representativeRole.getRole().getName();

        String token = jwtUtil.generateToken(user.getUsername(), roleName);

        return new LoginResponse(token, user.getUsername(), roleName, user.isEmailVerified());
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
