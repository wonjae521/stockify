package com.stock.stockify.domain.report;

import com.stock.stockify.domain.report.ReportRequestDto;
import com.stock.stockify.domain.report.ReportResponseDto;
import com.stock.stockify.domain.report.ReportService;
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