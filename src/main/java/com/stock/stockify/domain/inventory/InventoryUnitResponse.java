package com.stock.stockify.domain.inventory;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InventoryUnitResponse {

    private String barcodeId;
    private LocalDateTime expirationDate;
    private boolean isSold;

    public static InventoryUnitResponse from(InventoryUnit unit) {
        return InventoryUnitResponse.builder()
                .barcodeId(unit.getBarcodeId())
                .expirationDate(unit.getExpirationDate())
                .isSold(unit.isSold())
                .build();
    }
}
