package com.stock.stockify.domain.barcode;

import com.stock.stockify.domain.barcode.BarcodeScanRequest;
import com.stock.stockify.domain.barcode.BarcodeScanResponse;
import com.stock.stockify.domain.inventory.InventoryItem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/barcode")
@RequiredArgsConstructor
public class BarcodeController {

    private final BarcodeService barcodeService;

    @PostMapping("/scan")
    public ResponseEntity<BarcodeScanResponse> scanPost(@RequestBody BarcodeScanRequest request) throws Exception {
        InventoryItem item = barcodeService.processBarcode(request.getBarcode());
        return ResponseEntity.ok(
                new BarcodeScanResponse("처리 완료", item.getName(), item.getQuantity())
        );
    }

    @GetMapping("/scan")
    public ResponseEntity<BarcodeScanResponse> scanGet(@RequestParam String barcode) throws Exception {
        InventoryItem item = barcodeService.processBarcode(barcode);
        return ResponseEntity.ok(
                new BarcodeScanResponse("처리 완료", item.getName(), item.getQuantity())
        );
    }
}
