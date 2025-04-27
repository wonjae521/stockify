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
public class AiReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 보고서 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 보고서 내용

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 생성일시

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
