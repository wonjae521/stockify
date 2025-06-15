package com.stock.stockify.domain.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserId(Long userId);

    // List<UserRole> findByRoleName(String roleName);

    List<UserRole> findByUserIdAndWarehouseId(Long userId, Long warehouseId);

    void deleteByRole(Role role);

    Optional<UserRole> findByIdAndUserId(Long roleId, Long userId);
}
