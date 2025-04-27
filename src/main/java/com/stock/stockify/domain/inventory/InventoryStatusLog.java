package com.stock.stockify.domain.inventory;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_status_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private InventoryItem inventoryItem; // 입출고한 재고 아이템

    @Column(nullable = false)
    private String status; // "입고" 또는 "출고"

    @Column(nullable = false)
    private int quantity; // 입출고 수량

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 기록 생성 시간

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
