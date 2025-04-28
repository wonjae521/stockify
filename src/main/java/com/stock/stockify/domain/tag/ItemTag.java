package com.stock.stockify.domain.tag;

import jakarta.persistence.*;
import lombok.*;
import com.stock.stockify.domain.inventory.InventoryItem;

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
}

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ItemTagId implements java.io.Serializable {

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "tag_id")
    private Long tagId;
}