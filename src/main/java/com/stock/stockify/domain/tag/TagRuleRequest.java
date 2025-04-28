package com.stock.stockify.domain.tag;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagRuleRequest {

    @NotNull(message = "태그 ID는 필수입니다.")
    private Long tagId;

    @NotNull(message = "조건 타입은 필수입니다.")
    private TagRule.ConditionType conditionType;

    @NotNull(message = "조건 값은 필수입니다.")
    private String conditionValue;
}
