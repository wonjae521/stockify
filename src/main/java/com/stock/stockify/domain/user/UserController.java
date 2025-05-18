package com.stock.stockify.domain.user;

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

// 사용자(User) 관련 API를 처리하는 컨트롤러
// 회원가입, 로그인 기능 제공
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterRequest request) {
        userService.registerUser(request.getUsername(), request.getPassword(), request.getRole());
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
        emailVerificationService.sendPasswordResetToken(request.getUsername(), request.getEmail(), ip);

        return ResponseEntity.ok("비밀번호 재설정 링크가 이메일로 전송되었습니다.");
    }

    // 비밀번호 초기화
    @PatchMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetConfirmRequest request) {
        emailVerificationService.resetPasswordWithToken(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }



}
