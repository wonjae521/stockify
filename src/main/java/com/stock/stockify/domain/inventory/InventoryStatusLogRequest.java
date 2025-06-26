package com.stock.stockify.domain.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// 입출고 기록 생성 요청 시 사용하는 DTO
@Getter
@Setter
public class InventoryStatusLogRequest {

    @NotNull(message = "재고 ID는 필수입니다.")
    private Long inventoryItemId; // 기록할 재고 품목 ID

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private int quantity; // 입출고 수량

    @NotNull(message = "작업 종류는 필수입니다.")
    private Action action; // 작업 종류 (입고, 출고, 조정)
}
