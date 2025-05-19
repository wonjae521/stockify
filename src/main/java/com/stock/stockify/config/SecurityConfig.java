package com.stock.stockify.config;

import com.stock.stockify.global.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import java.util.List;

// Spring Security 보안 설정 클래스
// JWT 인증 기반으로 동작
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Spring Security의 필터 체인 설정
    // CORS 설정, CSRF 비활성화, 세션 사용 안 함 (STATELESS), URL별 인증 및 권한 설정, JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // CSRF 비활성화 (JWT는 세션을 쓰지 않기 때문)
                .csrf(csrf -> csrf.disable())
                // 세션 관리 전략: STATELESS (매 요청마다 토큰 검증)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll() // 회원가입 인증 없이 가능
                        .requestMatchers(HttpMethod.POST, "/api/users/login").permitAll()
                        .requestMatchers("/api/email/**").permitAll() // 이메일 인증 JWT 없이 가능
                        .requestMatchers(HttpMethod.POST, "/api/reports/generate").permitAll() // 보고서 생성 허용 - 로그인 인증 없이 가능
                        .requestMatchers(HttpMethod.POST, "/api/users/request-password-reset").permitAll() // 비밀번호 변경 , 메일 인증용 토큰 요청
                        .requestMatchers(HttpMethod.PATCH, "/api/users/reset-password").permitAll() // 비밀번호 재설정 JWT 없이 가능
                        .requestMatchers(HttpMethod.GET, "/api/users/reset-password").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/change-password").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/email/change-password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/email/change-password/submit").permitAll()
                        .requestMatchers(HttpMethod.POST, "/change-password-success").permitAll()
                        .requestMatchers(HttpMethod.GET, "/change-password-success").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/email/verify-password-token").permitAll()



                        // WebSocket 관련 예외 추가 (테스트용 HTML, 연결 엔드포인트, 메시지 송수신 경로 허용)
                        .requestMatchers(
                                "/ws-test.html",
                                "/ws-stockify/**",
                                "/topic/**",
                                "/app/**"
                        ).permitAll()

                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // admin만 접근 가능
                        .requestMatchers("/api/staff/**").hasRole("STAFF") // staff만 접근 가능
                        .requestMatchers("/api/inventories/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/report-test.html").permitAll()
                        .anyRequest().authenticated()
                )
                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 등록
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // 폼 로그인(formLogin) 비활성화 (토큰 인증을 쓰니까 필요 없음)
                // 폼 로그인(formLogin) 비활성화 (토큰 인증을 쓰니까 필요 없음)
                .formLogin(form -> form.disable()) // 폼 로그인(formLogin) 비활성화 (토큰 인증을 쓰니까 필요 없음)
                // HTTP Basic 인증 비활성화 (토큰 인증을 쓰니까 필요 없음)
                // HTTP Basic 인증 비활성화 (토큰 인증을 쓰니까 필요 없음)
                .httpBasic(basic -> basic.disable()); // HTTP Basic 인증 비활성화 (토큰 인증을 쓰니까 필요 없음);


        return http.build();
    }

    // CORS 허용 설정 -> 프론트엔드(또는 Postman)에서 오는 요청을 허용하기 위함
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 인증정보 포함 허용
        config.setAllowedOriginPatterns(List.of("*")); // 모든 Origin 허용 (Postman도 포함)
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // HTTP 메소드 허용
        config.setAllowedHeaders(List.of("*")); // 모든 Header 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 위 설정 적용
        return new CorsFilter(source);
    }

    // CORS 설정 소스 메소드 -> 위 corsFilter와 비슷하지만, SecurityFilterChain 설정에서 직접 사용하는 버전
    private UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // 비밀번호 암호화에 사용할 PasswordEncoder 빈 등록, BCrypt 해시 함수를 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
