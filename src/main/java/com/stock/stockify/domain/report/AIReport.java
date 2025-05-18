package com.stock.stockify.domain.report;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private String type; // 리포트 유형 (예: WEEKLY_REPORT)

    @Column(nullable = false)
    private String periodStart; // 분석 시작일

    @Column(nullable = false)
    private String periodEnd; // 분석 종료일

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 리포트 본문 (GPT 결과)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 작성 시간

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
