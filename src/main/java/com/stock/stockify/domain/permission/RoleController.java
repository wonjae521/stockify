
package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // 역할 생성
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> createRole(@RequestParam String name,
                                           @AuthenticationPrincipal User admin) {
        Role role = roleService.createRole(name, admin.getId());
        return ResponseEntity.ok(role);
    }

    // 역할에 권한 설정 (수정 포함)
    @PutMapping("/{roleId}/permissions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updatePermissions(@PathVariable Long roleId,
                                                  @RequestBody List<String> permissionCodes) {
        roleService.updatePermissions(roleId, permissionCodes);
        return ResponseEntity.ok().build();
    }

    // 역할 삭제
    @DeleteMapping("/{roleId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteRole(@PathVariable Long roleId,
                                           @AuthenticationPrincipal User admin) {
        roleService.deleteRole(roleId, admin.getId());
        return ResponseEntity.noContent().build();
    }

    // ADMIN이 만든 역할 목록 조회
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleResponseDto>> getMyRoles(@AuthenticationPrincipal User admin) {
        return ResponseEntity.ok(roleService.getRolesByAdmin(admin));
    }

}
