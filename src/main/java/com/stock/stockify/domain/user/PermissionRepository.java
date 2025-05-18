package com.stock.stockify.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
    List<Permission> findByNameIn(List<String> names); // ex: ["INVENTORY_MANAGE", "ORDER_MANAGE"]
}
