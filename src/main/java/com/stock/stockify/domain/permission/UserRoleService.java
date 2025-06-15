package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.user.UserRepository;
import com.stock.stockify.domain.warehouse.Warehouse;
import com.stock.stockify.domain.warehouse.WarehouseRepository;
import com.stock.stockify.global.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final WarehouseRepository warehouseRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;

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

    // 사용자 권한 조회
    public List<UserRoleResponseDto> getUserRolesWithPermissions(Long userId) {
        List<UserRole> userRoles = userRoleRepository.findByUserId(userId);

        return userRoles.stream().map(userRole -> {
            Role role = userRole.getRole();
            List<String> permissionNames = rolePermissionRepository.findByRole(role).stream()
                    .map(rp -> rp.getPermission().getName())
                    .toList();

            return new UserRoleResponseDto(
                    userRole.getWarehouse().getId(),
                    userRole.getWarehouse().getName(),
                    role.getName(),
                    permissionNames

            );

        }).toList();
    }

    // 사용자 역할 수정
    @Transactional
    public void updateUserRole(UserRoleUpdateRequest request) {
        UserRole userRole = userRoleRepository.findOneByUserIdAndWarehouseId(
                        request.getUserId(), request.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("해당 사용자의 역할을 찾을 수 없습니다."));

        Role newRole = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new NotFoundException("역할을 찾을 수 없습니다."));

        // ADMIN 역할은 설정할 수 없음
        if ("ADMIN".equalsIgnoreCase(newRole.getName())) {
            throw new IllegalArgumentException("ADMIN 역할로 변경할 수 없습니다.");
        }

        // 현재 역할이 ADMIN인 경우에도 변경 막기
        if ("ADMIN".equalsIgnoreCase(userRole.getRole().getName())) {
            throw new IllegalArgumentException("ADMIN 사용자의 역할은 수정할 수 없습니다.");
        }

        // 역할 변경 적용
        userRole.setRole(newRole);
    }

    // 사용자 역할 삭제
    @Transactional
    public void deleteUserRole(UserRoleDeleteRequest request) {
        userRoleRepository.deleteByUserIdAndWarehouseId(
                request.getUserId(), request.getWarehouseId());
    }


}
