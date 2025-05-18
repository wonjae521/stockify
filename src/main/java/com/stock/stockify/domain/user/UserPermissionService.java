package com.stock.stockify.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPermissionService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final UserPermissionRepository userPermissionRepository;

    // 권한 설정
    @Transactional
    public void updateUserPermissions(Long userId, List<String> permissionNames) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 기존 권한 삭제
        userPermissionRepository.deleteAllByUser_Id(userId);

        // 새로운 권한 목록 생성
        List<Permission> permissions = permissionRepository.findByNameIn(permissionNames);
        for (Permission permission : permissions) {
            UserPermission up = new UserPermission(user, permission);
            userPermissionRepository.save(up);
        }
    }

    // 권한 조회
    @Transactional(readOnly = true)
    public List<String> getUserPermissions(Long userId) {
        return userPermissionRepository.findByUser_Id(userId)
                .stream()
                .map(up -> up.getPermission().getName())
                .toList();
    }
}
