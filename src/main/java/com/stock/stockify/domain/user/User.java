package com.stock.stockify.domain.user;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // INT 자동 증가 (PK)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username; // 로그인용 아이디

    @Column(nullable = false)
    private String password; // 비밀번호

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // 사용자 역할

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 가입 날짜

    @PrePersist
    protected void onCreate() { // DB에 현재 시간 저장
        this.createdAt = LocalDateTime.now();
    }
}
