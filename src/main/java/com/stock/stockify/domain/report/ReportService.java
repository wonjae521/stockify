package com.stock.stockify.domain.report;

public interface ReportService {
    ReportResponseDto generateReport(ReportRequestDto request);
}