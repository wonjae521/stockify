package com.stock.stockify.domain.order;

import com.stock.stockify.domain.inventory.InventoryItem;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = true)
    private InventoryItem item;

    private int quantity;

    @Column(name = "price_at_order", nullable = false)
    private double priceAtOrder;
}
