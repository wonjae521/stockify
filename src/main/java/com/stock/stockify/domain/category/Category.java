package com.stock.stockify.domain.category;

import jakarta.persistence.*;
import lombok.*;

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
