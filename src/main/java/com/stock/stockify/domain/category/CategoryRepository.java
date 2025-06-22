package com.stock.stockify.domain.category;

import com.stock.stockify.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Category 엔티티에 대한 JPA 레포지토리, 기본 CRUD 메소드 제공
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // 이름으로 카테고리 조회
    Optional<Category> findByName(String name);

    // 이름, 소유주로 카테고리 조회
    Optional<Category> findByNameAndOwner(String name, User owner);

}
