package com.stock.stockify.domain.warehouse;

import com.stock.stockify.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserWarehouseRoleRepository extends JpaRepository<UserWarehouseRole, Long> {

    // 사용자별 창고 권한 조회
    List<UserWarehouseRole> findByUser(User user);

    // 특정 사용자-창고 권한 존재 여부 (중복 방지용)
    boolean existsByUserIdAndWarehouseId(Long userId, Long warehouseId);

    // 특정 사용자와 창고에 대한 권한 조회
    Optional<UserWarehouseRole> findByUserIdAndWarehouseId(Long userId, Long warehouseId);
}
