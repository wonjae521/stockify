package com.stock.stockify.domain.tag;

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

    public enum ConditionType {
        QUANTITY_BELOW, EXPIRING_SOON
    }
}