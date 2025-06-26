package com.stock.stockify.domain.report;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/generate")
    public ReportResponseDto generateReport(@RequestBody ReportRequestDto request) {
        return reportService.generateReport(request);
    }
}