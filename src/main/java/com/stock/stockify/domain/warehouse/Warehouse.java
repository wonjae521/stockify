package com.stock.stockify.domain.warehouse;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouses")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 창고 id

    @Column(nullable = false, unique = true, length = 100)
    private String name; // 창고 이름

    private String description; // 창고 부가 설명

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt; // 창고 탭 생성 시간

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
