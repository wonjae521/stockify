package com.stock.stockify.domain.user;

// import com.stock.stockify.global.email.EmailSenderService;
import com.stock.stockify.global.mail.EmailService;
import lombok.RequiredArgsConstructor;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final PasswordEncoder passwordEncoder;
    // private final EmailSenderService emailSenderService;
    private final EmailService emailService;


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

        tokenRepository.save(token);

        // 목적별 이메일 전송 분기
        if (purpose.equals("EMAIL_VERIFICATION")) {
            emailService.sendVerificationEmail(user.getEmail(), token.getToken());
        } else if (purpose.equals("PASSWORD_RESET")) {
            emailService.sendPasswordResetEmail(user.getEmail(), token.getToken());
        }

        return token;
    }

    // 이메일 인증 처리 (링크 클릭 시 호출)
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

    /**
    // 비밀번호 재설정용 토큰 검증(로그인x)
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
    */

    // 비밀번호 재설정용 토큰 검증
    public void verifyPasswordToken(String token) {
        EmailVerificationToken emailToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (emailToken.isVerified()) {
            throw new IllegalStateException("이미 사용된 토큰입니다.");
        }

        if (emailToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        if (!emailToken.getPurpose().equals("PASSWORD_RESET") && !emailToken.getPurpose().equals("PASSWORD_CHANGE")) {
            throw new IllegalStateException("비밀번호 변경용 토큰이 아닙니다.");
        }
    }

    /**
    // 비밀번호 재설정 처리(로그인x)
    @Transactional
    public void resetPasswordWithToken(String tokenString, String newPassword) {
        EmailVerificationToken token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (!token.getPurpose().equals("PASSWORD_RESET") && !token.getPurpose().equals("PASSWORD_CHANGE")) {
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
    */

    /**
    // 비밀번호 변경용 토큰 검증(로그인o)
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

    */
    // 비밀번호 변경 처리(로그인o)
    @Transactional
    public void changePasswordWithToken(String tokenString, String newPassword) {
        EmailVerificationToken token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (!token.getPurpose().equals("PASSWORD_RESET") && !token.getPurpose().equals("PASSWORD_CHANGE")) {
            throw new IllegalStateException("비밀번호 변경용 토큰이 아닙니다.");
        }

        if (token.isVerified()) {
            throw new IllegalStateException("이미 사용된 토큰입니다.");
        }

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        // 비밀번호 변경
        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        // 토큰 처리
        token.setVerified(true);
        token.setVerifiedAt(LocalDateTime.now());
    }

}
