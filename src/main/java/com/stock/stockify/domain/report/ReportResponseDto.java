package com.stock.stockify.domain.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDto {
    private String summary;
    private Map<String, Object> chartData; // 이 필드 필요 없으면 null 처리 가능
    private String GptMessage;
}
