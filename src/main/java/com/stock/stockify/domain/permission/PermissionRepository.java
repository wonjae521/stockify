package com.stock.stockify.domain.permission;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    boolean existsByName(String name);

    // 권한 코드 리스트로 권한 목록 조회
    List<Permission> findByNameIn(List<String> names);

    // 권한명으로 조회
    Optional<Permission> findByName(String name);

}
