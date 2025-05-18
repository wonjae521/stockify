package com.stock.stockify.domain.order;

import com.stock.stockify.domain.inventory.InventoryItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private InventoryItem item;

    private int quantity;

    @PrePersist
    public void prePersist() {
        this.id = UUID.randomUUID();
    }
}
