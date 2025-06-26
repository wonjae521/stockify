package com.stock.stockify.domain.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final AIReportRepository aiReportRepository;
    private final GPTService gptService; // GPT-4o API 호출 담당 서비스

    @Override
    public ReportResponseDto generateReport(ReportRequestDto request) {
        // 여기에 실제 통계 데이터를 JSON으로 생성하는 로직 추가 필요
        String jsonData = "{ /* 통계 데이터 JSON 변환 결과 */ }";

        // 프롬프트 생성
        String prompt = ReportPromptFactory.generatePrompt(
                request.getType(), jsonData, request.getPeriodStart(), request.getPeriodEnd()
        );

        // GPT 호출
        String gptReply = gptService.callChatGPT(prompt);

        // DB 저장용 엔티티 생성
        AIReport report = AIReport.builder()
                .title(request.getType().name() + " 보고서")
                .type(request.getType().name())
                .periodStart(request.getPeriodStart())
                .periodEnd(request.getPeriodEnd())
                .content(gptReply)
                .build();

        aiReportRepository.save(report);

        // 응답 DTO 반환
        return new ReportResponseDto("요약된 보고서", null, gptReply);
    }
}
