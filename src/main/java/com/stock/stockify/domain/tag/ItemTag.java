package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.inventory.InventoryItem;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

// 재고와 태그의 다대다 관계를 관리하는 중간 엔티티, 복합 키를 사용해 InventoryItem + Tag 쌍으로 유일성 보장
@Entity
@Table(name = "item_tags")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ItemTag {

    @EmbeddedId
    private ItemTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private InventoryItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    // isDeleted로 soft-delete 가능
    private boolean isDeleted;
}
