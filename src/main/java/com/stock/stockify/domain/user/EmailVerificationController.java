package com.stock.stockify.domain.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;


// @RestController
@Controller
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService verificationService;
    private final com.stock.stockify.global.email.EmailSenderService emailSenderService;

    /**
    // 회원가입 또는 이메일 인증 요청 시 호출
    @PostMapping("/send-verification")
    public String sendEmailVerification(@RequestParam Long userId, HttpServletRequest request) {
        // 요청 IP
        String ip = request.getRemoteAddr();

        // 토큰 생성
        EmailVerificationToken token = verificationService.generateToken(userId, ip, "EMAIL_VERIFICATION", 15);

        // 인증 URL 구성
        String verificationUrl = "http://localhost:8080/api/email/verify?token=" + token.getToken();

        // 이메일 전송
        emailSenderService.sendEmail(
                token.getUser().getEmail(),
                "[Stockify] 이메일 인증 요청",
                "다음 링크를 클릭하여 이메일 인증을 완료하세요:\n" + verificationUrl
        );

        return "이메일 인증 링크가 전송되었습니다.";
    }
    */

    // 인증 링크 클릭 시 호출되는 API
    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        verificationService.verifyToken(token);
        return "이메일 인증이 완료되었습니다.";
    }

    // 비밀번호 재설정
    @GetMapping("/verify-password-token")
    public ResponseEntity<String> verifyPasswordToken(@RequestParam String token) {
        verificationService.verifyPasswordToken(token);
        return ResponseEntity.ok("토큰이 유효합니다. 비밀번호 재설정 화면으로 이동하세요.");
    }

    /**
    // 비밀번호 변경 화면 진입 확인용 핸들러
    @GetMapping("/change-password")
    public ResponseEntity<String> handleChangePasswordPage(@RequestParam String token) {
        // 이 토큰은 이후 PATCH 비밀번호 변경 요청에 사용됨
        return ResponseEntity.ok("비밀번호 변경 토큰 확인 완료: " + token);
    }
    */

    // 비밀번호 변경 HTML 폼 보여주기
    @GetMapping("/change-password")
    public String showPasswordChangeForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "change-password-form";  // templates/change-password-form.html
    }

    // 비밀번호 변경 요청 처리
    @PostMapping("/change-password/submit")
    public String submitPasswordChangeForm(@RequestParam String token,
                                           @RequestParam String newPassword,
                                           RedirectAttributes redirectAttributes) {
        verificationService.changePasswordWithToken(token, newPassword);
        redirectAttributes.addFlashAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        return "redirect:/api/email/change-password-success";
    }

    @GetMapping("/change-password-success")
    public String showSuccessPage() {
        return "change-password-success"; // templates 폴더 내의 템플릿 이름
    }
}
