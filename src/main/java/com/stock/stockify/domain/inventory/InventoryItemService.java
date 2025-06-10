package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.category.Category;
import com.stock.stockify.domain.category.CategoryService;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // 로그인된 사용자 정보
    private User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getUser();  // 로그인된 사용자 정보 반환
        }
        throw new IllegalStateException("현재 로그인된 사용자가 없습니다.");
    }

    // 재고 등록 기능
    public InventoryItem createInventoryItem(Long warehouseId, InventoryItemRequest request) {

        // 이메일 미인증 사용자에 대한 제한
        User user = getCurrentUser(); // 여기서 user를 가져옴
        if (!user.isEmailVerified()) {
            long count = inventoryItemRepository.countByCreatedBy(user);
            if (count >= 10) {
                throw new AccessDeniedException("이메일 인증 전에는 최대 10개의 재고만 등록할 수 있습니다.");
            }
        }

        // category 이름으로 Category 엔티티 찾아오기
        Category category = categoryService.getCategoryByName(request.getCategory());

        // 재고 등록
        InventoryItem item = InventoryItem.builder()
                .name(request.getName())
                .quantity(request.getQuantity())
                .category(category)
                .price(request.getPrice())
                .warehouseId(warehouseId) // 창고 ID 반영
                .build();

        // DB에 저장
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
