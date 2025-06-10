package com.stock.stockify.domain.warehouse;

import com.stock.stockify.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final UserWarehouseRoleRepository userWarehouseRoleRepository;

    // 창고 생성
    public Warehouse createWarehouse(CreateWarehouseRequest request, User user) {
        if (!user.isEmailVerified()) {
            long count = userWarehouseRoleRepository.countByUser(user);
            if (count >= 1) {
                throw new AccessDeniedException("이메일 인증 전에는 하나의 창고만 생성할 수 있습니다.");
            }
        }

        Warehouse warehouse = Warehouse.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        warehouseRepository.save(warehouse);

        UserWarehouseRole link = UserWarehouseRole.builder()
                .user(user)
                .warehouse(warehouse)
                .roleType(user.getRoleType())
                .build();

        userWarehouseRoleRepository.save(link);

        return warehouse;
    }

    // 사용자가 관리하는 창고 목록 조회
    public List<Warehouse> getWarehouses(User user) {
        return userWarehouseRoleRepository.findByUser(user).stream()
                .map(UserWarehouseRole::getWarehouse)
                .collect(Collectors.toList());
    }

    // 창고 수정 (name, description 만)
    public Warehouse updateWarehouse(Long warehouseId, CreateWarehouseRequest request, User user) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new IllegalArgumentException("창고가 없습니다."));

        // 권한 검사: 사용자가 가장할 수 있는 창고인지 확인
        boolean hasAccess = userWarehouseRoleRepository.existsByUserAndWarehouse(user, warehouse);
        if (!hasAccess) {
            throw new AccessDeniedException("이 창고에 대해 수정 권한이 없습니다.");
        }

        warehouse.setName(request.getName());
        warehouse.setDescription(request.getDescription());
        return warehouseRepository.save(warehouse);
    }

    // 창고 삭제
    public void deleteWarehouse(Long warehouseId, User user) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new IllegalArgumentException("창고가 없습니다."));

        boolean hasAccess = userWarehouseRoleRepository.existsByUserAndWarehouse(user, warehouse);
        if (!hasAccess) {
            throw new AccessDeniedException("이 창고에 대해 삭제 권한이 없습니다.");
        }

        warehouseRepository.delete(warehouse);
    }
}
