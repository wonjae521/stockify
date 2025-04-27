package com.stock.stockify.domain.inventory;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    // 어떤 재고 품목에 대한 입출고 기록인지 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    @JsonIgnore // ✅ 여기에 붙여야 해!
    private InventoryItem inventoryItem;

    @Column(nullable = false)
    private int quantity; // 입출고 수량

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status; // IN(입고) or OUT(출고)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum Status {
        IN, OUT
    }
}
