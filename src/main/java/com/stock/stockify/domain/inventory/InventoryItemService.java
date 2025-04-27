package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.category.Category;
import com.stock.stockify.domain.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final CategoryService categoryService; // 🔥 추가

    // 🔥 재고 등록 기능
    public InventoryItem createInventoryItem(InventoryItemRequest request) {
        // 🔥 category 이름으로 Category 엔티티 찾아오기
        Category category = categoryService.getCategoryByName(request.getCategory());

        InventoryItem item = InventoryItem.builder()
                .name(request.getName())
                .quantity(request.getQuantity())
                .category(category) // 🔥 수정: String → Category 엔티티
                .build();
        return inventoryItemRepository.save(item);
    }

    // 🔥 재고 전체 조회 기능
    public List<InventoryItem> getAllInventoryItems() {
        return inventoryItemRepository.findAll();
    }

    // 🔥 재고 삭제 기능
    public void deleteInventoryItem(Long id) {
        inventoryItemRepository.deleteById(id);
    }
}
