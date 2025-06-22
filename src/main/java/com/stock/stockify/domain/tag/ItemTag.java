package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.inventory.InventoryItem;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

// 재고 항목과 태그 간의 다대다 연결 테이블 (복합 키)
@Entity
@Table(name = "item_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemTag {

    @EmbeddedId
    private ItemTagId id;

    @MapsId("itemId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private InventoryItem inventoryItem;

    @MapsId("tagId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    // 소유자(owner)는 InventoryItem과 Tag가 동일 ADMIN을 참조하므로 별도 필드 없이 상위에서 처리
}
