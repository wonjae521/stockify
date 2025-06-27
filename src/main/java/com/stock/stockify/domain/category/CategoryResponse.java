package com.stock.stockify.domain.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 카테고리 응답용 DTO
 * - 필요한 필드만 포함
 */
@Getter
@AllArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;

    // Category 엔티티를 DTO로 변환하는 정적 메서드
    public static CategoryResponse fromEntity(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}
