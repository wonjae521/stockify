package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 태그 자동 규칙을 관리하는 리포지토리
 */
public interface TagRuleRepository extends JpaRepository<TagRule, Long> {

    /** 특정 ADMIN 소유의 모든 규칙 조회 */
    List<TagRule> findByOwner(User owner);

    /** 특정 태그에 연결된 규칙들 조회 */
    List<TagRule> findByTag(Tag tag);
}
