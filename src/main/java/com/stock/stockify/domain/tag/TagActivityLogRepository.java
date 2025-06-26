package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.inventory.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 태그 작업 이력 기록을 위한 리포지토리
 */
public interface TagActivityLogRepository extends JpaRepository<TagActivityLog, Long> {

    /** 특정 재고 아이템에 대한 로그 조회 */
    List<TagActivityLog> findByInventoryItem(InventoryItem item);

    /** 특정 태그 기준 로그 조회 */
    List<TagActivityLog> findByTag(Tag tag);
}
