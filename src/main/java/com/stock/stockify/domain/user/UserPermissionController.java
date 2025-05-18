package com.stock.stockify.domain.user;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserPermissionController {

    private final UserPermissionService userPermissionService;

    // 권한 설정 (관리자만 가능)
    @PostMapping("/{userId}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePermissions(
            @PathVariable Long userId,
            @RequestBody UserPermissionRequest request) {
        userPermissionService.updateUserPermissions(userId, request.getPermissions());
        return ResponseEntity.ok().build();
    }

    // 권한 조회
    @GetMapping("/{userId}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserPermissionResponse> getPermissions(@PathVariable Long userId) {
        List<String> permissions = userPermissionService.getUserPermissions(userId);
        return ResponseEntity.ok(new UserPermissionResponse(userId, permissions));
    }
}

