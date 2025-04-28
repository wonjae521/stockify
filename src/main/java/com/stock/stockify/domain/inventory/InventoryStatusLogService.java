package com.stock.stockify.domain.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryStatusLogService {

    private final InventoryStatusLogRepository inventoryStatusLogRepository;
    private final InventoryItemRepository inventoryItemRepository;

    // 입출고 기록 생성
    public InventoryStatusLog createLog(InventoryStatusLogRequest request) {
        InventoryItem item = inventoryItemRepository.findById(request.getInventoryItemId())
                .orElseThrow(() -> new RuntimeException("해당 재고 아이템을 찾을 수 없습니다. ID: " + request.getInventoryItemId()));

        InventoryStatusLog log = InventoryStatusLog.builder()
                .inventoryItem(item)
                .quantity(request.getQuantity())
                .action(request.getAction())  // ⭐ 수정된 부분: request에서 Status enum 직접 받음
                .build();

        return inventoryStatusLogRepository.save(log);
    }

    // 입출고 기록 전체 조회
    public List<InventoryStatusLog> getAllLogs() {
        return inventoryStatusLogRepository.findAll();
    }
}
