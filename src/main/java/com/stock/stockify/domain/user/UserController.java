package com.stock.stockify.domain.user;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserRegisterRequest request) {
        userService.registerUser(request.getUsername(), request.getPassword(), request.getRole());
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginRequest request, HttpServletResponse response) {
        // 로그인 성공 → JWT 토큰 발급
        String token = userService.login(request.getUsername(), request.getPassword());

        // 응답 헤더에 추가 (⭐ 여기 수정된 부분 ⭐)
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        return ResponseEntity.ok("로그인 성공"); // body에는 "로그인 성공"만 보내기
    }
}
