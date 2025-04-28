package com.stock.stockify.domain.category;

import jakarta.persistence.*;
import lombok.*;

// 데이터베이스 categories 테이블과 매핑
// 재고 품목(InventoryItem)의 분류를 담당
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // INT 자동 증가
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name; // 카테고리 이름
}
