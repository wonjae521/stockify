package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.category.Category;
import com.stock.stockify.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.time.LocalDateTime;

// 재고 항목 엔티티
@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "is_deleted = false")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 재고명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private int quantity; // 수량

    @Column(name = "rfid_tag_id")
    private String rfidTagId;

    @Column(name = "barcode_id", unique = true)
    private String barcodeId;

    @Column(nullable = false)
    private Double price; // 단가

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private String unit; // 단위 (예: 개, 박스, L)

    @Column(name = "threshold_quantity", nullable = false)
    private int thresholdQuantity; // 최소 재고 경고 기준

    @Column(columnDefinition = "TEXT")
    private String memo; // 비고 메모

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner; // 소유자 - 무조건 ADMIN

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy; // 생성한 사용자

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    private User lastModifiedBy; // 마지막 수정 사용자
}