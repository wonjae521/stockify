package com.stock.stockify.domain.user;

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

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterRequest request) {
        userService.registerUser(request.getUsername(), request.getPassword(), request.getRole());
        return ResponseEntity.ok("회원가입 완료");
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginRequest request, HttpServletResponse response) {
        // 로그인 성공 → JWT 토큰 발급
        String token = userService.login(request.getUsername(), request.getPassword());

        // 응답 헤더에 추가
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        return ResponseEntity.ok("로그인 성공");
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

}
