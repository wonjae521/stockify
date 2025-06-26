package com.stock.stockify.domain.tag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 태그 생성 요청 DTO
 * - name: 태그 이름
 * - description: 설명 (선택)
 * - type: 태그 유형 (CUSTOM or SYSTEM)
 */
@Getter
@Setter
public class TagRequest {

    /** 태그 이름 (필수) */
    @NotBlank(message = "태그 이름은 필수입니다.")
    private String name;

    /** 태그 설명 (선택) */
    private String description;

    /** 태그 타입 (CUSTOM 또는 SYSTEM) */
    @NotNull(message = "태그 타입은 필수입니다.")
    private Tag.Type type;
}
