package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> assignRole(@RequestBody UserRoleRequestDto request,
                                           @AuthenticationPrincipal User admin) {
        userRoleService.assignRoleToUser(request, admin);
        return ResponseEntity.ok().build();
    }

    // 사용자 역할 수정(ex. STAFF -> SUBADMIN)
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateUserRole(@RequestBody @Valid UserRoleUpdateRequest request) {
        userRoleService.updateUserRole(request);

        return ResponseEntity.ok().build();
    }

}
