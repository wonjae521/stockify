package com.stock.stockify.domain.category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 필요하면 여기다가 메서드 추가 가능 (예: findByName 같은 거)
}
