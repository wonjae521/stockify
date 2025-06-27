package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.category.Category;
import com.stock.stockify.domain.category.CategoryRepository;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.global.exception.PermissionDeniedException;
import com.stock.stockify.global.security.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 재고 등록 (warehouseId는 controller에서 권한 검사용으로만 사용되고, 실제 저장 시 사용되지 않음)
     */
    @Transactional
    public InventoryItem createInventoryItem(Long warehouseId, InventoryItemRequest request) {
        User user = UserContext.getCurrentUser();

        if (!user.isEmailVerified()) {
            throw new PermissionDeniedException("이메일 인증 후 재고 등록이 가능합니다.");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        Long categoryOwnerId = category.getOwner().getAdmin() != null ?
                category.getOwner().getAdmin().getId() : category.getOwner().getId();
        Long userAdminId = user.getAdmin() != null ? user.getAdmin().getId() : user.getId();

        if (!categoryOwnerId.equals(userAdminId)) {
            throw new PermissionDeniedException("해당 카테고리에 접근할 수 없습니다.");
        }

        Optional<InventoryItem> existing = inventoryItemRepository.findByNameAndOwner(request.getName(), user);

        if (existing.isPresent()) {
            InventoryItem existingItem = existing.get();
            List<InventoryUnit> newUnits = request.getUnits().stream()
                    .map(unitReq -> InventoryUnit.builder()
                            .item(existingItem)
                            .expirationDate(unitReq.getExpirationDate())
                            .barcodeId(unitReq.getBarcodeId())
                            .isSold(false)
                            .build())
                    .collect(Collectors.toList());
            existingItem.getUnits().addAll(newUnits);
            return inventoryItemRepository.save(existingItem);
        } else {
            InventoryItem item = InventoryItem.builder()
                    .name(request.getName())
                    .unit(request.getUnit())
                    .thresholdQuantity(request.getThresholdQuantity())
                    .price(request.getPrice())
                    .category(category)
                    .owner(user)
                    .memo(request.getMemo())
                    .isDeleted(false)
                    .build();
            List<InventoryUnit> units = request.getUnits().stream()
                    .map(unitReq -> InventoryUnit.builder()
                            .item(item)
                            .expirationDate(unitReq.getExpirationDate())
                            .barcodeId(unitReq.getBarcodeId())
                            .isSold(false)
                            .build())
                    .collect(Collectors.toList());
            item.setUnits(units);
            return inventoryItemRepository.save(item);
        }
    }

    /**
     * 재고 전체 조회 (창고 ID 기준 관리자 소유 필터링)
     */
    public List<InventoryItemResponse> getItemsByWarehouse(Long warehouseId) {
        User user = UserContext.getCurrentUser();
        Long userAdminId = user.getAdmin() != null ? user.getAdmin().getId() : user.getId();

        return inventoryItemRepository.findAll().stream()
                .filter(item -> {
                    Long ownerId = item.getOwner().getAdmin() != null ?
                            item.getOwner().getAdmin().getId() : item.getOwner().getId();
                    return ownerId.equals(userAdminId) && !item.isDeleted();
                })
                .map(InventoryItemResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 재고 단일 조회
     */
    public InventoryItemResponse getItem(Long itemId) {
        User user = UserContext.getCurrentUser();

        InventoryItem item = inventoryItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다."));

        // 관리자 소유자 일치 여부 확인
        Long itemAdminId = item.getOwner().getAdmin() != null ?
                item.getOwner().getAdmin().getId() : item.getOwner().getId();

        Long userAdminId = user.getAdmin() != null ?
                user.getAdmin().getId() : user.getId();

        if (!itemAdminId.equals(userAdminId)) {
            throw new PermissionDeniedException("해당 재고에 접근할 수 없습니다.");
        }

        // 삭제 여부 확인
        if (item.isDeleted()) {
            throw new IllegalArgumentException("이미 삭제된 재고입니다.");
        }

        return InventoryItemResponse.from(item);
    }

    /**
     * 재고 수정
     */
    @Transactional
    public void updateInventoryItem(Long warehouseId, Long itemId, InventoryItemRequest request) {
        User user = UserContext.getCurrentUser();
        InventoryItem item = inventoryItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다."));

        Long ownerId = item.getOwner().getAdmin() != null ?
                item.getOwner().getAdmin().getId() : item.getOwner().getId();
        Long userAdminId = user.getAdmin() != null ? user.getAdmin().getId() : user.getId();

        if (!ownerId.equals(userAdminId)) {
            throw new PermissionDeniedException("수정 권한이 없습니다.");
        }

        item.setName(request.getName());
        item.setUnit(request.getUnit());
        item.setThresholdQuantity(request.getThresholdQuantity());
        item.setPrice(request.getPrice());
        item.setMemo(request.getMemo());
        inventoryItemRepository.save(item);
    }

    /**
     * 재고 삭제 (soft-delete)
     */
    @Transactional
    public void deleteInventoryItem(Long itemId) {
        User user = UserContext.getCurrentUser();
        InventoryItem item = inventoryItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다."));

        Long ownerId = item.getOwner().getAdmin() != null ?
                item.getOwner().getAdmin().getId() : item.getOwner().getId();
        Long userAdminId = user.getAdmin() != null ? user.getAdmin().getId() : user.getId();

        if (!ownerId.equals(userAdminId)) {
            throw new PermissionDeniedException("삭제 권한이 없습니다.");
        }

        item.setDeleted(true);
        inventoryItemRepository.save(item);
    }
}
