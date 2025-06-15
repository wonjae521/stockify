package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.user.UserRepository;
import com.stock.stockify.domain.warehouse.Warehouse;
import com.stock.stockify.domain.warehouse.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRoleRepository userRoleRepository;

    // 특정 유저에게 창고별 역할 부여
    @Transactional
    public void assignRoleToUser(UserRoleRequestDto request, User admin) {
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("역할이 존재하지 않습니다."));

        if (!role.getAdmin().getId().equals(admin.getId())) {
            throw new AccessDeniedException("본인이 만든 역할만 할당할 수 있습니다.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new IllegalArgumentException("창고를 찾을 수 없습니다."));

        UserRole userRole = UserRole.builder()
                .user(user)
                .warehouse(warehouse)
                .role(role)
                .build();

        userRoleRepository.save(userRole);
    }

}
