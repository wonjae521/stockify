package com.stock.stockify.domain.user;

import com.stock.stockify.global.email.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final PasswordEncoder passwordEncoder;


    // 토큰 생성 및 저장
    @Transactional
    public EmailVerificationToken generateToken(Long userId, String ipAddress, String purpose, long minutesUntilExpire) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 기존 인증되지 않은 동일 purpose 토큰 제거 (중복 인증 방지)
        tokenRepository.findByUserIdAndPurposeAndVerifiedFalse(userId, purpose)
                .ifPresent(tokenRepository::delete);

        EmailVerificationToken token = EmailVerificationToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .purpose(purpose)
                .expiresAt(LocalDateTime.now().plusMinutes(minutesUntilExpire))
                .ipAddress(ipAddress)
                .build();

        return tokenRepository.save(token);
    }

    // 인증 처리 (링크 클릭 시 호출)
    @Transactional
    public void verifyToken(String tokenString) {
        EmailVerificationToken token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (token.isVerified()) {
            throw new IllegalStateException("이미 인증이 완료된 토큰입니다.");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        token.setVerified(true);
        token.setVerifiedAt(LocalDateTime.now());

        // 사용자의 email_verified = true 로 설정
        User user = token.getUser();
        user.setEmailVerified(true);
    }

    @Transactional
    public void sendPasswordResetToken(String username, String email, String ipAddress) {
        // 사용자 조회 및 검증
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!user.getEmail().equals(email)) {
            throw new IllegalArgumentException("이메일이 사용자 정보와 일치하지 않습니다.");
        }

        // 토큰 생성
        EmailVerificationToken token = generateToken(user.getId(), ipAddress, "PASSWORD_RESET", 15);

        // 이메일 발송
        String url = "http://localhost:8080/api/users/verify-password-token?token=" + token.getToken();

        emailSenderService.sendEmail(
                user.getEmail(),
                "[Stockify] 비밀번호 재설정 안내",
                "아래 링크를 클릭해 비밀번호를 재설정하세요:\n" + url
        );
    }

    @Transactional(readOnly = true)
    public void verifyPasswordResetToken(String tokenString) {
        EmailVerificationToken token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (!token.getPurpose().equals("PASSWORD_RESET")) {
            throw new IllegalStateException("비밀번호 재설정용 토큰이 아닙니다.");
        }

        if (token.isVerified()) {
            throw new IllegalStateException("이미 사용된 토큰입니다.");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }
    }

    @Transactional
    public void resetPasswordWithToken(String tokenString, String newPassword) {
        EmailVerificationToken token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (!token.getPurpose().equals("PASSWORD_RESET")) {
            throw new IllegalStateException("비밀번호 재설정용 토큰이 아닙니다.");
        }

        if (token.isVerified()) {
            throw new IllegalStateException("이미 사용된 토큰입니다.");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        // 비밀번호 변경
        User user = token.getUser();
        String encoded = passwordEncoder.encode(newPassword);
        user.setPassword(encoded);

        // 토큰 처리
        token.setVerified(true);
        token.setVerifiedAt(LocalDateTime.now());
    }

    // 비밀번호 변경 링크 전송
    @Transactional
    public void sendPasswordChangeToken(Long userId, String ipAddress) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        EmailVerificationToken token = generateToken(userId, ipAddress, "PASSWORD_CHANGE", 15);

        String link = "http://localhost:8080/api/users/verify-password-change-token?token=" + token.getToken();

        emailSenderService.sendEmail(
                user.getEmail(),
                "[Stockify] 비밀번호 변경 인증",
                "다음 링크를 클릭하여 비밀번호 변경을 완료하세요:\n" + link
        );
    }

    // 비밀번호 변경용 토큰 검증
    @Transactional(readOnly = true)
    public void verifyPasswordChangeToken(String tokenString) {
        EmailVerificationToken token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (!token.getPurpose().equals("PASSWORD_CHANGE")) {
            throw new IllegalStateException("비밀번호 변경용 토큰이 아닙니다.");
        }

        if (token.isVerified()) {
            throw new IllegalStateException("이미 사용된 토큰입니다.");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }
    }

    // 비밀번호 변경
    @Transactional
    public void changePasswordWithToken(String tokenString, String newPassword) {
        EmailVerificationToken token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (!token.getPurpose().equals("PASSWORD_CHANGE")) {
            throw new IllegalStateException("비밀번호 변경용 토큰이 아닙니다.");
        }

        if (token.isVerified()) {
            throw new IllegalStateException("이미 사용된 토큰입니다.");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        token.setVerified(true);
        token.setVerifiedAt(LocalDateTime.now());
    }

}
