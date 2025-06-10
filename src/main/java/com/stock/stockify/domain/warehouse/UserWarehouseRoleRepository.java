package com.stock.stockify.domain.warehouse;

import com.stock.stockify.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserWarehouseRoleRepository extends JpaRepository<UserWarehouseRole, Long> {

    long countByUser(User user);

    // 🔹 사용자 소속 창고 전체 조회용
    List<UserWarehouseRole> findByUser(User user);

    // 🔹 사용자에게 특정 창고에 대한 접근 권한이 있는지 확인용
    boolean existsByUserAndWarehouse(User user, Warehouse warehouse);
}
