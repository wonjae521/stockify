package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.inventory.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * InventoryItem과 Tag 사이 다대다 관계를 다루는 리포지토리
 */
public interface ItemTagRepository extends JpaRepository<ItemTag, ItemTagId> {

    /** 재고 아이템에 부착된 모든 태그 연결 조회 */
    List<ItemTag> findByInventoryItem(InventoryItem item);

    /** 특정 태그가 부착된 모든 재고 조회 */
    List<ItemTag> findByTag(Tag tag);

    /** 특정 재고-태그 연결 여부 확인 */
    boolean existsByInventoryItemAndTag(InventoryItem item, Tag tag);

    /** 특정 재고에 연결된 태그 제거 */
    void deleteByInventoryItem(InventoryItem item);
}
