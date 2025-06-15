package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.warehouse.Warehouse;
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

    Optional<UserRole> findOneByUserIdAndWarehouseId(Long userId, Long warehouseId);

    Optional<UserRole> findByUserAndWarehouse(User user, Warehouse warehouse);



    void deleteByUserIdAndWarehouseId(Long userId, Long warehouseId);




}
