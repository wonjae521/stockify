package com.stock.stockify.global.security;

import com.stock.stockify.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// 매 요청(Request)마다 동작하는 JWT 인증 필터
// HTTP 요청 헤더에서 JWT 토큰을 읽어 유효한 경우 Spring Security 인증(SecurityContext)에 저장
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // 실제 필터링 작업을 수행하는 메소드, 요청마다 실행
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 요청 헤더에서 Authorization 값을 가져온다
        String authHeader = request.getHeader("Authorization");

        // Authorization 헤더가 존재하고, "Bearer "로 시작하는 경우
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = jwtUtil.parseToken(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            // 사용자 권한 리스트 추출
            List<String> permissions = claims.get("permissions", List.class);

            if (username != null && role != null) {
                // Spring Security에서 사용 가능한 인증 객체 생성, ROLE_ 기반 기본 역할 권한 생성
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                // JWT에 포함된 permission을 GrantedAuthority로 변환
                List<SimpleGrantedAuthority> permissionAuthorities = permissions != null ?
                        permissions.stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList())
                        : List.of();

                // 역할 + 세부 권한 모두 포함한 authorities 목록
                List<SimpleGrantedAuthority> authorities = permissionAuthorities.stream()
                        .collect(Collectors.toList());
                authorities.add(authority); // ROLE_ADMIN 또는 ROLE_STAFF도 포함

                // Spring Security에서 사용 가능한 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(authority));

                // 추가 요청 정보 설정 (IP 주소, 세션 ID 등)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 현재 요청을 인증된 상태로 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 토큰이 없더라도 다음 필터로 넘어가야 함
        filterChain.doFilter(request, response);
    }
}