package com.stock.stockify.domain.user;

import com.stock.stockify.domain.permission.*;
import com.stock.stockify.global.mail.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 사용자(User) 관련 API를 처리하는 컨트롤러
// 회원가입, 로그인 기능 제공
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;
    private final UserRoleService userRoleService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterRequest request) {
        userService.registerUser(request.getUsername(), // 아이디
                                 request.getPassword(), // 비밀번호
                                 request.getEmail());   // 이메일
        return ResponseEntity.ok("회원가입 완료");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid UserLoginRequest request, HttpServletResponse response) {
        // 로그인 성공 → JWT 토큰 + 사용자 정보 반환
        LoginResponse loginResponse = userService.login(request.getUsername(), request.getPassword());

        // 응답 헤더에 토큰 추가
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + loginResponse.getToken());

        // 응답 바디에 사용자 정보와 인증 상태 포함
        return ResponseEntity.ok(loginResponse);
    }


    // 아이디 수정
    @PatchMapping("/me/username")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateUsername(@RequestBody UsernameUpdateRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getUserIdFromUserDetails(userDetails);
        userService.updateUsername(userId, request.getNewUsername());
        return ResponseEntity.ok("아이디가 성공적으로 변경되었습니다.");
    }

    // 비밀번호 수정
    @PatchMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest request,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = userService.getUserIdFromUserDetails(userDetails);
        userService.updatePassword(userId, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    // 비밀번호 초기화 요청
    @PostMapping("/request-password-reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody @Valid PasswordResetRequest request,
                                                       HttpServletRequest servletRequest) {
        // 클라이언트 IP
        String ip = servletRequest.getRemoteAddr();

        // 사용자 확인 후 이메일 전송
        Long userId = userService.getUserIdByUsernameAndEmail(request.getUsername(), request.getEmail());
        emailVerificationService.generateToken(userId, ip, "PASSWORD_RESET", 15);



        return ResponseEntity.ok("비밀번호 재설정 링크가 이메일로 전송되었습니다.");
    }

    // 비밀번호 초기화
    @PatchMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetConfirmRequest request) {
        emailVerificationService.changePasswordWithToken(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    // 비밀번호 변경 링크 전송
    @PostMapping("/request-password-change")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> requestPasswordChange(HttpServletRequest servletRequest,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        String ip = servletRequest.getRemoteAddr();
        Long userId = userService.getUserIdFromUserDetails(userDetails);
        emailVerificationService.generateToken(userId, ip, "PASSWORD_CHANGE", 15);

        return ResponseEntity.ok("비밀번호 변경 링크가 이메일로 전송되었습니다.");
    }

    // 비밀번호 변경 토큰 검증
    @GetMapping("/verify-password-change-token")
    public ResponseEntity<String> verifyPasswordChangeToken(@RequestParam String token) {
        emailVerificationService.verifyPasswordToken(token);
        return ResponseEntity.ok("토큰이 유효합니다. 비밀번호 변경 화면으로 이동하세요.");
    }

    // 비밀번호 변경
    @PatchMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request) {
        emailVerificationService.changePasswordWithToken(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }

    // 사용자 역할 + 권한 조회
    @GetMapping("/{userId}/roles")
    public ResponseEntity<List<UserRoleResponseDto>> getUserRoles(@PathVariable Long userId) {
        List<UserRoleResponseDto> roles = userRoleService.getUserRolesWithPermissions(userId);
        return ResponseEntity.ok(roles);
    }

    // 사용자 역할 수정
    @PutMapping("/user-roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserRole(@RequestBody UserRoleUpdateRequest request) {
        userRoleService.updateUserRole(request);
        return ResponseEntity.ok("사용자 역할이 변경되었습니다.");
    }

    // 사용자 역할 삭제
    @DeleteMapping("/user-roles")
    public ResponseEntity<?> deleteUserRole(@RequestBody UserRoleDeleteRequest request) {
        userRoleService.deleteUserRole(request);
        return ResponseEntity.ok("사용자 역할이 삭제되었습니다.");
    }

    // 전체 사용자 목록 조회
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserSummaryResponseDto>> getAllUsers(@AuthenticationPrincipal User currentAdmin) {
        return ResponseEntity.ok(userService.getAllUsersByAdmin(currentAdmin));
    }
}
