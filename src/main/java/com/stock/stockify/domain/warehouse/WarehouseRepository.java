package com.stock.stockify.domain.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    // 창고 이름 중복 방지용
    Optional<Warehouse> findByName(String name);

    boolean existsByName(String name);
}
