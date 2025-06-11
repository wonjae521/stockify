package com.stock.stockify.domain.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByRole(Role role);

    boolean existsByRoleAndPermission(Role role, Permission permission);

    @Query("SELECT rp FROM RolePermission rp JOIN FETCH rp.role JOIN FETCH rp.permission")
    List<RolePermission> findAllWithRoleAndPermission(); // LAZY 오류 해결용

    Optional<RolePermission> findByRoleAndPermission(Role role, Permission permission);
}
