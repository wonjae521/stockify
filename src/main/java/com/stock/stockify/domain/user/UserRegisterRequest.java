package com.stock.stockify.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// 회원가입 요청 데이터를 받는 DTO
// 아이디, 비밀번호, 역할(role)을 입력받는다
@Setter
@Getter
public class UserRegisterRequest {

    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 4, max = 20, message = "아이디는 4~20자여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, max = 30, message = "비밀번호는 6~30자여야 합니다.")
    private String password;

    @NotNull(message = "역할(role)은 필수입니다.")
    private UserRole role;
}
