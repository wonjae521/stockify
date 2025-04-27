package com.stock.stockify.domain.report;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class AiReportController {

    private final AiReportService aiReportService;

    @PostMapping
    public ResponseEntity<AiReport> createReport(@RequestParam String title,
                                                 @RequestParam String content) {
        AiReport report = aiReportService.createReport(title, content);
        return ResponseEntity.ok(report);
    }

    @GetMapping
    public ResponseEntity<List<AiReport>> getAllReports() {
        List<AiReport> reports = aiReportService.getAllReports();
        return ResponseEntity.ok(reports);
    }
}
