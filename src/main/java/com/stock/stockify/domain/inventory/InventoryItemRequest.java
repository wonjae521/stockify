package com.stock.stockify.domain.inventory;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 재고 항목 생성 요청을 받을 때 사용하는 DTO
@Getter
@NoArgsConstructor
public class InventoryItemRequest {

    private String name;                  // 상품명
    private Integer quantity;             // 수량
    private String category;              // 카테고리
    private Double price;                 // 가격
    private String barcodeId;             // 바코드 id
    private String rfidTagId;             // RFID 태그 id
    private LocalDateTime expirationDate; // 유통기한
    private String unit;                  // 단위 (예: 개, 박스, L 등)
    private int thresholdQuantity;        // 최소 재고 기준(나중에 알림쓸 때 사용)
    private String memo;                  // 비고 메모
}
