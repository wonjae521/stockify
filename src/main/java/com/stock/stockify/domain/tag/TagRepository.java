package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 태그 엔티티 관련 JPA 리포지토리
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    /** 특정 ADMIN이 소유한 태그 전체 조회 */
    List<Tag> findByOwner(User owner);

    /** 특정 ADMIN + 이름으로 태그 조회 (중복 방지용 등) */
    Tag findByNameAndOwner(String name, User owner);
}
