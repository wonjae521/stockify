package com.stock.stockify.domain.rtls;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

// 위치 기록 Entity
@Entity
@Table(name = "rtls_user_locations")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RtlsUserLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private double x;

    @Column(nullable = false)
    private double y;

    @Column(nullable = false)
    private double z;

    @Column(name = "detected_at", nullable = false)
    private LocalDateTime detectedAt = LocalDateTime.now();
}
