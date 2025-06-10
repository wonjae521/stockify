package com.stock.stockify.domain.inventory;

import com.stock.stockify.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.stock.stockify.domain.category.Category;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

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
    private String name; // 재고명

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // 카테고리

    @Column(nullable = false)
    private int quantity; // 수량

    @Column(name = "rfid_tag_id")
    private String rfidTagId; // rfid태그 id

    @Column(name = "barcode_id", unique = true)
    private String barcodeId; // 바코드 id

    @Column(nullable = false)
    private Double price; // 가격

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId; // 창고id(위치)

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate; // 유통기한

    @Column(name = "created_at")
    private LocalDateTime createdAt; // 생성 시간

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 갱신 시간

    // 엔티티 저장 전 생성/수정일시 자동 설정
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy; // 재고 추가한 직원

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by")
    private User lastModifiedBy; // 마지막으로 수정한 직원

    // 엔티티 수정 전 수정일시 자동 갱신
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}