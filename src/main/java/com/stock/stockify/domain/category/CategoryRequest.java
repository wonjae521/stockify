package com.stock.stockify.domain.category;

import lombok.Getter;

// 카테고리 생성 요청 시 사용하는 DTO
@Getter
public class CategoryRequest {
    private String name; // 생성할 카테고리 이름
}
