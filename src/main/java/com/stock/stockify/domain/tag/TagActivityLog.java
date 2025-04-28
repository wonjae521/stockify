package com.stock.stockify.domain.tag;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.stock.stockify.domain.inventory.InventoryItem;
import com.stock.stockify.domain.user.User;

@Entity
@Table(name = "tag_activity_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private InventoryItem inventoryItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Action action; // ADDED, REMOVED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by")
    private User performedBy;

    @Column(name = "performed_at")
    private LocalDateTime performedAt;

    @PrePersist
    protected void onCreate() {
        this.performedAt = LocalDateTime.now();
    }

    public enum Action {
        ADDED, REMOVED
    }
}