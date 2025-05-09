package com.stock.stockify.domain.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 로그인 요청 데이터를 받는 DTO
// 아이디와 비밀번호를 입력받는다
@Getter
@NoArgsConstructor
public class UserLoginRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
