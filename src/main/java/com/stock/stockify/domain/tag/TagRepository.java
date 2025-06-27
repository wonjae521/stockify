package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 태그 엔티티 기본 Repository
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    // 중복 태그 방지용
    boolean existsByNameAndOwnerAndIsDeletedFalse(String name, User owner);

    // 해당 ADMIN이 소유한 태그 전체 조회
    List<Tag> findByOwnerAndIsDeletedFalse(User owner);

    // 삭제되지 않은 태그만 단일 조회
    Optional<Tag> findByIdAndIsDeletedFalse(Long id);
}
