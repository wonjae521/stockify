package com.stock.stockify.domain.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        // 재고 수가 0개일 때 로그 생성 불가
        if (inventoryItemRepository.count() == 0) {
            throw new IllegalStateException("입출고 기록을 남기기 위해서는 먼저 재고를 등록해야 합니다.");
        }

        InventoryItem item = inventoryItemRepository.findById(request.getInventoryItemId())
                .orElseThrow(() -> new RuntimeException("해당 재고 아이템을 찾을 수 없습니다."));

        InventoryStatusLog log = InventoryStatusLog.builder()
                .inventoryItem(item)
                .action(request.getAction())
                .quantity(request.getQuantity())
                .triggeredBy(null) // 사용자 정보 나중에 연동 예정
                .timestamp(LocalDateTime.now())
                .warehouseId(item.getWarehouseId()) // 이 부분이 있어야 동작 가능
                .build();

        return inventoryStatusLogRepository.save(log);
    }

    // 입출고 기록 전체 조회
    public List<InventoryStatusLog> getAllLogs() {
        return inventoryStatusLogRepository.findAll();
    }
}
