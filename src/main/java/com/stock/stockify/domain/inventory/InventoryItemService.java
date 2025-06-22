package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.category.Category;
import com.stock.stockify.domain.category.CategoryService;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.global.exception.NotFoundException;
import com.stock.stockify.global.auth.PermissionChecker;
import com.stock.stockify.global.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
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
    private final PermissionChecker permissionChecker;

    // 재고 등록 또는 중복 시 수량만 추가 (owner 기준)
    public InventoryItem createInventoryItem(Long warehouseId, InventoryItemRequest request) {
        User user = UserContext.getCurrentUser();
        User owner = user.getAdminOwner();
        permissionChecker.check(user.getId(), "INVENTORY_WRITE");

        // 이메일 미인증 사용자는 10개까지만 등록 가능
        if (!user.isEmailVerified()) {
            long count = inventoryItemRepository.countByCreatedBy(user);
            if (count >= 10) {
                throw new AccessDeniedException("이메일 인증 전에는 최대 10개의 재고만 등록할 수 있습니다.");
            }
        }

        Category category = categoryService.getCategoryByNameAndOwner(request.getCategory(), owner);

        return inventoryItemRepository
                .findByNameAndPriceAndUnitAndWarehouseIdAndOwner(
                        request.getName(), request.getPrice(), request.getUnit(), warehouseId, owner)
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + request.getQuantity());
                    existing.setUpdatedAt(java.time.LocalDateTime.now());
                    return existing;
                })
                .orElseGet(() -> inventoryItemRepository.save(
                        InventoryItem.builder()
                                .name(request.getName())
                                .quantity(request.getQuantity())
                                .category(category)
                                .price(request.getPrice())
                                .unit(request.getUnit())
                                .thresholdQuantity(request.getThresholdQuantity())
                                .warehouseId(warehouseId)
                                .rfidTagId(request.getRfidTagId())
                                .barcodeId(request.getBarcodeId())
                                .expirationDate(request.getExpirationDate())
                                .memo(request.getMemo())
                                .owner(owner)
                                .createdBy(user)
                                .build()
                ));
    }

    // 재고 목록 조회 (해당 ADMIN의 소유 재고만)
    public List<InventoryItemResponse> getItemsByWarehouse(Long warehouseId) {
        User user = UserContext.getCurrentUser();
        User owner = user.getAdminOwner();
        permissionChecker.check(user.getId(), "INVENTORY_VIEW");

        return inventoryItemRepository.findByWarehouseIdAndOwner(warehouseId, owner).stream()
                .map(InventoryItemResponse::from)
                .toList();
    }

    // 재고 수정 기능
    public void updateItem(Long warehouseId, Long itemId, InventoryItemRequest request) {
        User user = UserContext.getCurrentUser();
        User owner = user.getAdminOwner();
        permissionChecker.check(user.getId(), "INVENTORY_UPDATE");

        InventoryItem item = inventoryItemRepository.findByIdAndOwner(itemId, owner)
                .orElseThrow(() -> new NotFoundException("해당 재고를 찾을 수 없습니다."));

        item.setName(request.getName());
        item.setQuantity(request.getQuantity());
        item.setPrice(request.getPrice());
        item.setCategory(categoryService.getCategoryByNameAndOwner(request.getCategory(), owner));
        item.setUnit(request.getUnit());
        item.setThresholdQuantity(request.getThresholdQuantity());
        item.setMemo(request.getMemo());
    }

    // 재고 삭제 (Soft Delete)
    public void deleteItem(Long itemId) {
        User user = UserContext.getCurrentUser();
        User owner = user.getAdminOwner();
        permissionChecker.check(user.getId(), "INVENTORY_DELETE");

        InventoryItem item = inventoryItemRepository.findByIdAndOwner(itemId, owner)
                .orElseThrow(() -> new NotFoundException("해당 재고를 찾을 수 없습니다."));

        item.setDeleted(true); // 소프트 삭제 처리
    }

    // 단일 조회
    public InventoryItemResponse getItem(Long itemId) {
        User user = UserContext.getCurrentUser();
        User owner = user.getAdminOwner();
        permissionChecker.check(user.getId(), "INVENTORY_VIEW");

        InventoryItem item = inventoryItemRepository.findByIdAndOwner(itemId, owner)
                .orElseThrow(() -> new NotFoundException("해당 재고를 찾을 수 없습니다."));

        return InventoryItemResponse.from(item);
    }
}
