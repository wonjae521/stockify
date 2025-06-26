package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.inventory.InventoryItem;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryItemResponse {

    private Long id;           // 재고 ID
    private String name;       // 품목명
    private String category;   // 카테고리명
    private int quantity;      // 수량
    private String rfidTagId;  // RFID 태그
    private String barcodeId;  // 바코드
    private double price;      // 가격

    // InventoryItem 엔티티 → DTO 변환
    public static InventoryItemResponse from(InventoryItem item) {
        return InventoryItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .category(item.getCategory().getName())
                .quantity(item.getQuantity())
                .rfidTagId(item.getRfidTagId())
                .barcodeId(item.getBarcodeId())
                .price(item.getPrice())
                .build();
    }
}
