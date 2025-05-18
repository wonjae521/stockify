package com.stock.stockify.domain.order;

import com.stock.stockify.domain.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 주문(Order) 정보를 나타내는 JPA 엔티티입니다.
 * - 사용자, 주문 상태, 주문 항목 등의 정보를 포함합니다.
 */
@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    // 주문 ID (int 자동 생성)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // 주문한 사용자 (다대일 관계)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 주문 시간
    private LocalDateTime orderDate;

    // 주문 상태 (문자열로 저장)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 주문 메모
    private String note;

    // 주문에 포함된 품목들 (일대다 관계)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    // 엔티티가 영속되기 전 자동으로 값 설정
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.REQUESTED;
    }

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;




    /**
     * 주문 상태 열거형
     * - REQUESTED: 요청됨
     * - PROCESSING: 처리 중
     * - COMPLETED: 완료됨
     * - CANCELLED: 취소됨
     */
    public enum OrderStatus {
        REQUESTED, PROCESSING, COMPLETED, CANCELLED
    }
}
