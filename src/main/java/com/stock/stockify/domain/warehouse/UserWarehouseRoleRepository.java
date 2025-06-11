package com.stock.stockify.domain.warehouse;

import com.stock.stockify.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserWarehouseRoleRepository extends JpaRepository<UserWarehouseRole, Long> {

    long countByUser(User user);

    // 사용자가 관리하는 창고 전체 조회용
    List<UserWarehouseRole> findByUser(User user);

    // 사용자와 특정 창고에 소속됐는지 확인(유저, 창고 통째로 사용)
    Optional<UserWarehouseRole> findByUserAndWarehouse(User user, Warehouse warehouse);

    // 사용자와 특정 창고에 소속됐는지 확인(유저ID, 창고ID 사용)
    @Query("SELECT uwr FROM UserWarehouseRole uwr WHERE uwr.user.id = :userId AND uwr.warehouse.id = :warehouseId")
    Optional<UserWarehouseRole> findByUserAndWarehouseId(@Param("userId") Long userId, @Param("warehouseId") Long warehouseId);


    // 사용자에게 특정 창고에 대한 접근 권한이 있는지 확인용
    boolean existsByUserAndWarehouse(User user, Warehouse warehouse);

    // 창고 삭제용
    void deleteByWarehouse(Warehouse warehouse);
}
