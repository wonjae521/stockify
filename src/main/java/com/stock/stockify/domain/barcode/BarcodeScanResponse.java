package com.stock.stockify.domain.barcode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BarcodeScanResponse {
    private String message;
    private String productName;
    private int quantity;
}

