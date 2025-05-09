package com.stock.stockify.domain.report;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 데이터베이스 ai_reports 테이블과 매핑
// 리포트 제목, 내용, 작성일시를 관리
@Entity
@Table(name = "ai_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AIReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 리포트 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 리포트 본문 (내용)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 작성 시간

    // 리포트 저장 전에 작성시간 자동 세팅
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
