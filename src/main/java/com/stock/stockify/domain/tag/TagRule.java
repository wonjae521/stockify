package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

// 조건 기반 자동 태그 부여 규칙 엔티티, 예시: "재고 수량 < 5"일 때 "부족" 태그 자동 부여
@Entity
@Table(name = "tag_rules")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TagRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 조건 표현식 (예: quantity < 5)
    @NotBlank(message = "조건은 필수입니다.")
    private String condition;

    // 적용할 태그 이름 (직접 연결 아님)
    @NotBlank(message = "태그 이름은 필수입니다.")
    private String tagName;

    // 생성한 ADMIN (권한 검사 및 격리)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

    // isDeleted로 soft-delete 가능
    private boolean isDeleted;
}
