package com.stock.stockify.domain.inventory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stock.stockify.domain.category.Category;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 재고 이름
    @Column(nullable = false)
    private String name;

    // 단위 (예: 개, 박스)
    private String unit;

    // 수량 임계값 (알림 기준)
    private Integer thresholdQuantity;

    // 단가
    private Integer price;

    // 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // 소유자 (최상위 관리자)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    // 개별 재고 단위들
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InventoryUnit> units = new ArrayList<>();

    // 실제 재고 수량 (units.size()로 대체 가능)
    @JsonIgnore
    public int getQuantity() {
        return (int) units.stream().filter(unit -> !unit.isSold()).count();
    }

    // 메모
    private String memo;

    // 삭제 여부 (soft delete)
    @Column(nullable = false)
    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    // 생성 시각
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
