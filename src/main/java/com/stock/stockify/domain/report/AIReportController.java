package com.stock.stockify.domain.report;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// AIReport 관련 API 요청을 처리하는 컨트롤러
// 리포트 생성, 조회, 삭제 기능 제공
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class AIReportController {

    private final AIReportService aiReportService;

    // 리포트 생성
    @PostMapping
    public ResponseEntity<AIReport> createReport(@RequestBody @Valid AIReportRequest request) {
        AIReport report = AIReport.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        return ResponseEntity.ok(aiReportService.createReport(report));
    }

    // 모든 리포트 조회
    @GetMapping
    public ResponseEntity<List<AIReport>> getAllReports() {
        return ResponseEntity.ok(aiReportService.getAllReports());
    }

    // 특정 ID로 리포트 조회
    @GetMapping("/{id}")
    public ResponseEntity<AIReport> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(aiReportService.getReportById(id));
    }

    // 리포트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        aiReportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
