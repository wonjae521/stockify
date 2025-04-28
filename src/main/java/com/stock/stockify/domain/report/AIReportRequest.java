package com.stock.stockify.domain.report;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// 리포트 생성 요청 시 사용하는 DTO
@Getter
@Setter
public class AIReportRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title; // 리포트 제목

    @NotBlank(message = "내용은 필수입니다.")
    private String content; // 리포트 내용
}
