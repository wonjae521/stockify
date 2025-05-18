package com.stock.stockify.domain.inventory;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// 재고 입출고 및 조정 기록을 저장하는 테이블(inventory_status_logs)과 매핑
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
    @JoinColumn(name = "item_id", nullable = true)
    private InventoryItem inventoryItem;  // 기록 대상 재고 품목

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Action action; // 작업 종류 (입고 INBOUND, 출고 OUTBOUND, 조정 ADJUSTMENT)

    @Column(nullable = false)
    private int quantity; // 입출고 수량

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triggered_by")
    private com.stock.stockify.domain.user.User triggeredBy; // 기록한 사용자

    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp; // 기록 생성 시각

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }

    // 입출고 작업 종류를 정의하는 열거형(Enum)
    public enum Action {
        INBOUND, OUTBOUND, ADJUSTMENT // 입고, 출고, 조정
    }
}
