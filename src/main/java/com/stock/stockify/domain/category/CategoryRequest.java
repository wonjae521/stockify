package com.stock.stockify.domain.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 카테고리 생성 요청 DTO
 */
@Getter
@Setter
public class CategoryRequest {

    /** 생성할 카테고리 이름 */
    @NotBlank(message = "카테고리 이름은 필수입니다.")
    private String name;
}
