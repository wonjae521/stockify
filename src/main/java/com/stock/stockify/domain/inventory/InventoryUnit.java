package com.stock.stockify.domain.inventory;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 개별 재고 단위 (예: 개별 제품, 유통기한, 바코드 등 관리)
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class InventoryUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 해당 단위가 속한 재고 품목
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id")
    private InventoryItem item;

    // 유통기한
    private LocalDateTime expirationDate;

    // 바코드 ID
    @Column(unique = true, length = 16) // 최대 30자
    @Size(min = 5, max = 16, message = "바코드 ID는 최대 16자까지 입력 가능합니다.")
    private String barcodeId;

    // RFID ID
    @Column(unique = true, length = 16)
    @Size(min = 5, max = 16, message = "RFID ID는 최대 16자까지 입력 가능합니다.")
    private String rfidId;

    // 출고 여부
    private boolean isSold;

    // 생성 시각 (입고 시점 기록용)
    @CreationTimestamp
    private LocalDateTime createdAt;

}
