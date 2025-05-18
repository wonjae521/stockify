package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.category.Category;
import com.stock.stockify.domain.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 재고 항목 관련 비즈니스 로직 처리 서비스
@Service
@RequiredArgsConstructor
@Transactional
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final CategoryService categoryService;

    // 재고 등록 기능
    public InventoryItem createInventoryItem(Long warehouseId, InventoryItemRequest request) {
        // category 이름으로 Category 엔티티 찾아오기
        Category category = categoryService.getCategoryByName(request.getCategory());

        InventoryItem item = InventoryItem.builder()
                .name(request.getName())
                .quantity(request.getQuantity())
                .category(category)
                .price(request.getPrice())
                .warehouseId(warehouseId) // 창고 ID 반영
                .build();

        return inventoryItemRepository.save(item);
    }

    // 재고 전체 조회 기능
    public List<InventoryItemResponse> getItemsByWarehouse(Long warehouseId) {
        return inventoryItemRepository.findByWarehouseId(warehouseId).stream()
                .map(InventoryItemResponse::from)
                .toList();
    }


    // 재고 수정 기능
    public void updateItem(Long warehouseId, Long itemId, InventoryItemRequest request) {
        InventoryItem item = inventoryItemRepository.findByIdAndWarehouseId(itemId, warehouseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 재고 항목이 존재하지 않습니다."));

        item.setName(request.getName());
        item.setQuantity(request.getQuantity());
        item.setPrice(request.getPrice());
        item.setCategory(categoryService.getCategoryByName(request.getCategory()));
    }

    // 재고 삭제 기능
    public void deleteItem(Long warehouseId, Long itemId) {
        InventoryItem item = inventoryItemRepository.findByIdAndWarehouseId(itemId, warehouseId)
                .orElseThrow(() -> new IllegalArgumentException("해당 재고 항목이 존재하지 않습니다."));
        inventoryItemRepository.delete(item);
    }
}
