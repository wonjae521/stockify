package com.stock.stockify.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPermissionRepository extends JpaRepository<UserPermission, UserPermissionId> {
    List<UserPermission> findByUser_Id(Long userId);
    void deleteAllByUser_Id(Long userId);
}
