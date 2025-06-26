package com.stock.stockify.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

// JWT 토큰 생성, 패싱하는 유틸리티 클래스
@Component
public class JwtUtil {

    // 비밀키(HS256 알고리즘 기반)
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // 토큰 만료 시간 1일
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    // JWT 토큰 생성 메소드
    public String generateToken(String username, String role) {
        return Jwts.builder() // 생성된 JWT 토큰 문자열
                .setSubject(username) // 토큰 제목(사용자명)
                .claim("role", role) // 추가 데이터(역할)
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
                .signWith(key) // 서명
                .compact(); // 최종 토큰 문자열로 압축
    }

    // JWT 토큰 패싱(검증) 메소드
    public Claims parseToken(String token) { // token-> 클라이언트가 보낸 JWT 토큰
        return Jwts.parserBuilder()// 토큰 안에 들어있는 정보들
                .setSigningKey(key) // 서명 검증용 키
                .build()
                .parseClaimsJws(token) // 토큰 패싱 및 검증
                .getBody(); // 본문 반환
    }
}
