
package com.stock.stockify.domain.permission;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // 특정 ADMIN이 만든 역할 목록 조회
    List<Role> findByAdminId(Long adminId);

    List<Role> findByName(String name);

    Optional<Role> findByNameAndAdminId(String name, Long adminId);


}
