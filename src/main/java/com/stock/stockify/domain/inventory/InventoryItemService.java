package com.stock.stockify.domain.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    // 📌 1. 재고 상품 등록
    public InventoryItem createItem(InventoryItem item) {
        return inventoryItemRepository.save(item);
    }

    // 📌 2. 재고 상품 전체 조회
    public List<InventoryItem> getAllItems() {
        return inventoryItemRepository.findAll();
    }

    // 📌 3. 재고 상품 삭제
    public void deleteItem(Long id) {
        inventoryItemRepository.deleteById(id);
    }
}
