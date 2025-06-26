package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 재고 입출고 및 수량 변화 기록을 위한 엔티티
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

    // 기록 대상 재고 항목
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private InventoryItem inventoryItem;

    // 작업 종류: INBOUND, OUTBOUND, ADJUSTMENT 등
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Action action;

    // 수량 변화
    @Column(nullable = false)
    private int quantity;

    // 기록을 만든 사용자 (직원)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triggered_by", nullable = false)
    private User triggeredBy;

    // 이 로그가 속한 ADMIN (owner 기준으로 분리)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // 기록 생성 시각
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Long warehouseId;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.timestamp = LocalDateTime.now();
    }
}
