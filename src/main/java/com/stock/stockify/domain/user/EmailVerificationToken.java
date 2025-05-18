package com.stock.stockify.domain.user;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 인증 대상 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 고유한 인증 토큰
    @Column(nullable = false, unique = true, length = 255)
    private String token;

    // 인증 목적: EMAIL_VERIFICATION, PASSWORD_RESET 등
    @Column(nullable = false, length = 50)
    private String purpose;

    // 인증 완료 여부
    @Column(nullable = false)
    private boolean verified = false;

    // 만료 시간
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // 인증 완료 시각
    private LocalDateTime verifiedAt;

    // 인증 요청 IP 주소
    private String ipAddress;

    // 생성 시각
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
