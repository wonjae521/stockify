package com.stock.stockify.domain.permission;

import jakarta.persistence.*;
import lombok.*;

// 직무
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // "ADMIN", "STAFF", "SUBADMIN"

    private String description; // 설명: 예) 전체 관리자, 재고 담당 등
}
