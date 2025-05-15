package com.stock.stockify.domain.inventory;

import lombok.Getter;
import lombok.NoArgsConstructor;

// 재고 항목 생성 요청을 받을 때 사용하는 DTO
@Getter
@NoArgsConstructor
public class InventoryItemRequest {

    private String name; // 상품명
    private Integer quantity; // 수량
    private String category; // 카테고리
    private Double price; // 가격
}
