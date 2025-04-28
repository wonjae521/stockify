package com.stock.stockify.domain.notification;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.stock.stockify.domain.inventory.InventoryItem;

// 재고(InventoryItem)와 연결된 알림 데이터를 관리
// 재고 부족, 초과, 이상 감지 시 알림 생성
@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private InventoryItem item; // 알림이 연결된 재고 항목

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    private AlertType alertType; // 알림 유형 (재고 부족, 초과, 이상 등)

    @Column(columnDefinition = "TEXT")
    private String message; // 알림 상세 메시지

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 알림 생성 시각

    @Column(name = "resolved")
    private boolean resolved; // 알림 읽음 여부

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.resolved = false;
    }
    // 알림의 종류를 정의하는 열거형(Enum)
    public enum AlertType {
        LOW_STOCK, OVERSTOCK, ANOMALY
    }
}