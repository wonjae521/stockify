package com.stock.stockify.domain.inventory;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 개별 재고 단위 요청 DTO
 */
@Data
public class InventoryUnitRequest {

    // 유통기한
    private LocalDateTime expirationDate;

    // 바코드
    private String barcodeId;

    // RFID ID
    private String rfidId;
}
