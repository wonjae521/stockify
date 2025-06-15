
package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;


    // ADMIN이 새로운 역할 생성
    @Transactional
    public Role createRole(String roleName, Long adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("관리자를 찾을 수 없습니다."));

        Role role = Role.builder()
                .name(roleName)
                .admin(admin)
                .build();

        return roleRepository.save(role);
    }

    // 역할에 권한 설정 또는 업데이트
    @Transactional
    public void updatePermissions(Long roleId, List<String> permissionCodes) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("역할을 찾을 수 없습니다."));

        // 기존 권한 제거
        rolePermissionRepository.deleteByRole(role);

        // 새로운 권한 추가
        List<Permission> permissions = permissionRepository.findByNameIn(permissionCodes);
        for (Permission permission : permissions) {
            RolePermission rp = RolePermission.builder()
                    .role(role)
                    .permission(permission)
                    .build();
            rolePermissionRepository.save(rp);
        }
    }

    // 역할 조회
    @Transactional
    public List<RoleResponseDto> getRolesByAdmin(User admin) {
        List<Role> roles = roleRepository.findByAdminId(admin.getId());
        return roles.stream()
                .map(RoleResponseDto::from)
                .toList();
    }


    // 역할 삭제 (자기 역할만 삭제 가능)
    @Transactional
    public void deleteRole(Long roleId, Long adminId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("역할을 찾을 수 없습니다."));

        if (!role.getAdmin().getId().equals(adminId)) {
            throw new SecurityException("본인이 만든 역할만 삭제할 수 있습니다.");
        }

        rolePermissionRepository.deleteByRole(role);
        userRoleRepository.deleteByRole(role);
        roleRepository.delete(role);
    }

    // ADMIN이 만든 모든 역할 조회
    public List<Role> getRolesByAdmin(Long adminId) {
        return roleRepository.findByAdminId(adminId);
    }
}
