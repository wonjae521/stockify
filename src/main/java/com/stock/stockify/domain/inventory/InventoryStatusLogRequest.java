package com.stock.stockify.domain.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryStatusLogRequest {

    @NotNull(message = "재고 ID는 필수입니다.")
    private Long inventoryItemId;

    @NotNull(message = "수량은 필수입니다.")
    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private int quantity;

    @NotNull(message = "작업 종류는 필수입니다.") // ✅ 수정
    private InventoryStatusLog.Action action; // ✅ 여기 Status → Action 으로 수정
}
