package com.stock.stockify.domain.warehouse;

import com.stock.stockify.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_warehouse_roles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "warehouse_id"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWarehouseRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 창고 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse;

    // 권한들
    private boolean canManageInventory = false;
    private boolean canManageOrders = false;
    private boolean canViewReports = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
