package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tag_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type", nullable = false)
    private ConditionType conditionType;

    @Column(name = "condition_value", nullable = false)
    private String conditionValue;

    @Column(nullable = false)
    private Boolean enabled = true;

    // 소유자: 이 규칙은 특정 ADMIN에 귀속됨
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public enum ConditionType {
        QUANTITY_BELOW, EXPIRING_SOON
    }
}
