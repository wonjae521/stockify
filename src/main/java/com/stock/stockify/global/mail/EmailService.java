package com.stock.stockify.global.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // 회원가입 후 이메일 인증 메일
    public void sendVerificationEmail(String to, String token) {
        String subject = "Stockify 이메일 인증 요청";
        String verificationUrl = "http://localhost:8080/api/email/verify?token=" + token;
        String text = String.format("""
            안녕하세요, Stockify입니다.

            아래 링크를 클릭하여 이메일 인증을 완료해주세요:

            %s

            감사합니다.
            """, verificationUrl);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    // 비밀번호 재설정(로그인x)
    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Stockify 비밀번호 재설정 안내";
        String resetLink = "http://localhost:8080/api/email/change-password?token=" + token;
        String text = String.format("""
            안녕하세요, Stockify 입니다.

            아래 링크를 클릭하여 비밀번호를 재설정해주세요:

            %s

            ※ 이 링크는 15분간 유효합니다.

            감사합니다.
            """, resetLink);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    // 비밀번호 변경(로그인o)
    public void sendPasswordChangeEmail(String to, String token) {
        String subject = "Stockify 비밀번호 변경 안내";
        String changeLink = "http://localhost:8080/api/email/change-password?token=" + token;
        String text = String.format("""
        안녕하세요, Stockify 입니다.

        아래 링크를 클릭하여 비밀번호를 변경해주세요:

        %s

        ※ 이 링크는 15분간 유효합니다.

        감사합니다.
        """, changeLink);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
