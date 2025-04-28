package com.stock.stockify.domain.notification;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.stock.stockify.domain.inventory.InventoryItem;

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
    private InventoryItem item;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    private AlertType alertType;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "resolved")
    private boolean resolved;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.resolved = false;
    }

    public enum AlertType {
        LOW_STOCK, OVERSTOCK, ANOMALY
    }
}