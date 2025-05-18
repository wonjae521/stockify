package com.stock.stockify.domain.rtls;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 위치 기록 Entity
@Entity
@Table(name = "rtls_item_locations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RtlsItemLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private double x;

    @Column(nullable = false)
    private double y;

    @Column(nullable = false)
    private double z;

    @Column(name = "detected_at", nullable = false)
    private LocalDateTime detectedAt = LocalDateTime.now();
}
