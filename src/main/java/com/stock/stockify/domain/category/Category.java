package com.stock.stockify.domain.category;

import com.stock.stockify.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * 카테고리 엔티티
 * - 재고 품목의 분류를 담당
 * - ADMIN 사용자 단위로 데이터 분리
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    /** 고유 ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 카테고리 이름 (ADMIN 소유자 내에서 고유) */
    @Column(nullable = false, length = 100)
    private String name;

    /** 소유자 (항상 ADMIN) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
}
