package com.stock.stockify.domain.report;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class AIReportController {

    private final AIReportService aiReportService;

    // ✅ AI 리포트 생성
    @PostMapping
    public ResponseEntity<AIReport> createReport(@RequestBody @Valid AIReportRequest request) {
        AIReport report = AIReport.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        return ResponseEntity.ok(aiReportService.createReport(report));
    }

    // ✅ 모든 AI 리포트 조회
    @GetMapping
    public ResponseEntity<List<AIReport>> getAllReports() {
        return ResponseEntity.ok(aiReportService.getAllReports());
    }

    // ✅ 특정 ID로 AI 리포트 조회
    @GetMapping("/{id}")
    public ResponseEntity<AIReport> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(aiReportService.getReportById(id));
    }

    // ✅ AI 리포트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        aiReportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
