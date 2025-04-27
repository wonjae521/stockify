package com.stock.stockify.domain.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryStatusLogService {

    private final InventoryStatusLogRepository inventoryStatusLogRepository;
    private final InventoryItemRepository inventoryItemRepository;

    public InventoryStatusLog createLog(Long itemId, String status, int quantity) {
        InventoryItem item = inventoryItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("해당 재고 아이템이 존재하지 않습니다."));

        InventoryStatusLog log = InventoryStatusLog.builder()
                .inventoryItem(item)
                .status(status)
                .quantity(quantity)
                .build();

        return inventoryStatusLogRepository.save(log);
    }

    public List<InventoryStatusLog> getAllLogs() {
        return inventoryStatusLogRepository.findAll();
    }
}
