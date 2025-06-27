package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.inventory.InventoryItem;
import com.stock.stockify.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 태그 추가/제거 등 활동 로그 엔티티
 * 누가 어떤 재고에 어떤 태그를 언제 적용했는지 기록
 */
@Entity
@Table(name = "tag_activity_logs")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TagActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 연관된 재고 항목
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private InventoryItem item;

    /**
     * 적용된 태그
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    /**
     * 수행한 사용자 (보통 ADMIN 또는 SUBADMIN)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 활동 타입: ADD / REMOVE
     */
    private String action;

    /**
     * 태그 적용 시각
     */
    private LocalDateTime timestamp;
}
