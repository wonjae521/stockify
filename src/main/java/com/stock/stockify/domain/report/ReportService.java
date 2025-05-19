package com.stock.stockify.domain.report;

import com.stock.stockify.domain.report.ReportRequestDto;
import com.stock.stockify.domain.report.ReportResponseDto;

public interface ReportService {
    ReportResponseDto generateReport(ReportRequestDto request);
}