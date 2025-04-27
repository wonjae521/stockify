package com.stock.stockify.domain.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryStatusLogRequest {

    @NotNull(message = "재고 ID는 필수입니다.")
    private Long inventoryItemId; // 어떤 재고 품목에 대한 입출고인지

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private int quantity; // 입출고 수량

    @NotNull(message = "상태는 필수입니다.")
    private InventoryStatusLog.Status status; // IN 또는 OUT (⭐ Enum 직접 받음)
}
