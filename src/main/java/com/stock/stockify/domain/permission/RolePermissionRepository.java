package com.stock.stockify.domain.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findByRole(Role role);

    // 특정 Role 엔티티 기준으로 권한 일괄 삭제
    void deleteByRole(Role role);

    // 특정 Role과 Permission 조합이 존재하는지 확인
    boolean existsByRoleAndPermission(Role role, Permission permission);

    // Role과 Permission 정보를 함께 조회 (LAZY 방지용)
    @Query("SELECT rp FROM RolePermission rp JOIN FETCH rp.role JOIN FETCH rp.permission")
    List<RolePermission> findAllWithRoleAndPermission(); // LAZY 오류 해결용

    // 특정 UserRole과 Permission에 해당하는 RolePermission 단건 조회
    Optional<RolePermission> findByRoleAndPermission(Role role, Permission permission);
}
