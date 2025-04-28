package com.stock.stockify.domain.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// InventoryStatusLog 관련 비즈니스 로직 처리 서비스 클래스
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryStatusLogService {

    private final InventoryStatusLogRepository inventoryStatusLogRepository;
    private final InventoryItemRepository inventoryItemRepository;

    // 입출고 기록 생성
    public InventoryStatusLog createLog(InventoryStatusLogRequest request) {
        // 재고 조회
        InventoryItem item = inventoryItemRepository.findById(request.getInventoryItemId())
                .orElseThrow(() -> new RuntimeException("해당 재고 아이템을 찾을 수 없습니다. ID: " + request.getInventoryItemId()));
        // 입출고 기록 생성
        InventoryStatusLog log = InventoryStatusLog.builder()
                .inventoryItem(item)
                .quantity(request.getQuantity())
                .action(request.getAction())
                .build();

        return inventoryStatusLogRepository.save(log);
    }

    // 입출고 기록 전체 조회
    public List<InventoryStatusLog> getAllLogs() {
        return inventoryStatusLogRepository.findAll();
    }
}
