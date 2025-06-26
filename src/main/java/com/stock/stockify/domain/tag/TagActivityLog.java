package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.inventory.InventoryItem;
import com.stock.stockify.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 태그 부착/해제 기록 로그
 * - 어떤 아이템에 어떤 태그가 언제 어떤 방식으로 적용되었는지를 기록
 */
@Entity
@Table(name = "tag_activity_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagActivityLog {

    /** 고유 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 대상 재고 아이템 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private InventoryItem inventoryItem;

    /** 적용된 태그 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    /** 태그 작업 종류 (부착 / 해제) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Action action;

    /** 작업 수행자 (직원, 관리자 등) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by")
    private User performedBy;

    /** 작업 수행 시각 */
    @Column(name = "performed_at")
    private LocalDateTime performedAt;

    /** 저장 전 자동 수행: 현재 시간 기록 */
    @PrePersist
    protected void onCreate() {
        this.performedAt = LocalDateTime.now();
    }

    /**
     * 로그 타입 정의 (부착 ADDED / 해제 REMOVED)
     */
    public enum Action {
        ADDED, REMOVED
    }
}
