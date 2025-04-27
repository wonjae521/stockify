package com.stock.stockify.domain.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AiReportService {

    private final AiReportRepository aiReportRepository;

    public AiReport createReport(String title, String content) {
        AiReport report = AiReport.builder()
                .title(title)
                .content(content)
                .build();
        return aiReportRepository.save(report);
    }

    public List<AiReport> getAllReports() {
        return aiReportRepository.findAll();
    }
}
