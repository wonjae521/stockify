package com.stock.stockify.domain.user;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// 데이터베이스 users 테이블과 매핑
// 회원의 아이디, 비밀번호, 역할, 가입일 등을 관리
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // INT 자동 증가 (PK)

    @Column(nullable = false, unique = true, length = 100)
    private String username; // 로그인용 아이디

    @Column(nullable = false)
    private String password; // 비밀번호

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role; // 사용자 역할

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 가입 날짜

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPermission> userPermissions = new ArrayList<>(); // 사용자 권한

    @Column(nullable = false, unique = true)
    private String email; // 이메일

    @Column(nullable = false)
    private boolean emailVerified = false; // 이메일 인증 여부

    @PrePersist
    protected void onCreate() { // DB에 현재 시간 저장
        this.createdAt = LocalDateTime.now();
    } // 가입 시간
}
