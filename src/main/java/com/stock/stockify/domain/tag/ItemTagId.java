package com.stock.stockify.domain.tag;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ItemTagId implements Serializable {

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "tag_id")
    private Long tagId;
}
