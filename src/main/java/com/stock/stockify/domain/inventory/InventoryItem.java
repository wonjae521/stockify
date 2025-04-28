package com.stock.stockify.domain.inventory;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.stock.stockify.domain.category.Category;

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
    private Long id;

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

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

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
}