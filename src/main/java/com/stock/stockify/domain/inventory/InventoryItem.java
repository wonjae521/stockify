package com.stock.stockify.domain.inventory;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.stock.stockify.domain.category.Category;

// 재고 이름, 수량, 카테고리, 가격, RFID, 바코드, 생성/수정시간 등을 관리
@Entity
@Table(name = "inventory_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 ID

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "rfid_tag_id")
    private String rfidTagId;

    @Column(name = "barcode_id", unique = true)
    private String barcodeId;

    @Column(nullable = false)
    private Double price;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 엔티티 저장 전 생성/수정일시 자동 설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    // 엔티티 수정 전 수정일시 자동 갱신
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}