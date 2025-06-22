package com.stock.stockify.domain.tag;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 태그 자동 부착 규칙 생성 요청 DTO
 * - tagId: 적용할 태그 ID
 * - conditionType: 조건 유형 (예: 수량 미만, 유통기한 임박 등)
 * - conditionValue: 조건 비교 기준값 (예: 수량, 일수)
 */
@Getter
@Setter
public class TagRuleRequest {

    /** 대상 태그 ID */
    @NotNull(message = "태그 ID는 필수입니다.")
    private Long tagId;

    /** 조건 유형 (예: QUANTITY_BELOW, EXPIRING_SOON) */
    @NotNull(message = "조건 타입은 필수입니다.")
    private TagRule.ConditionType conditionType;

    /** 조건 비교값 (예: 숫자나 날짜 등 문자열로 처리) */
    @NotNull(message = "조건 값은 필수입니다.")
    private String conditionValue;
}
