package com.stock.stockify.domain.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// AIReport 관련 비즈니스 로직을 처리하는 서비스 클래스

@Service
@RequiredArgsConstructor
@Transactional
public class AIReportService {

    private final AIReportRepository aiReportRepository;

    // 리포트 생성
    public AIReport createReport(AIReport report) {
        return aiReportRepository.save(report);
    }

    // 리포트 전체 조회
    public List<AIReport> getAllReports() {
        return aiReportRepository.findAll();
    }

    // 특정 ID로 리포트 조회
    public AIReport getReportById(Long id) {
        return aiReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("리포트를 찾을 수 없습니다. id=" + id));
    }

    // 특정 ID로 리포트 삭제
    public void deleteReport(Long id) {
        aiReportRepository.deleteById(id);
    }
}
