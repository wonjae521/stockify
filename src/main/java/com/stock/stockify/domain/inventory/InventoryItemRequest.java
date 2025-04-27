package com.stock.stockify.domain.inventory;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InventoryItemRequest {

    private String name; // 상품명
    private Integer quantity; // 수량
    private String category; // 카테고리

    // 필요하면 생성자나 빌더 추가 가능
}
