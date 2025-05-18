package com.stock.stockify.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    // 토큰으로 인증 정보 조회
    Optional<EmailVerificationToken> findByToken(String token);

    // 아직 인증되지 않은 사용자 토큰 조회 (중복 요청 방지 등)
    Optional<EmailVerificationToken> findByUserIdAndPurposeAndVerifiedFalse(Long userId, String purpose);

    // 만료된 토큰 삭제 (선택적 관리용)
    void deleteByExpiresAtBefore(java.time.LocalDateTime now);
}
