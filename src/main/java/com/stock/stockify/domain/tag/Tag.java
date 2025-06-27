package com.stock.stockify.domain.tag;

import com.stock.stockify.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "tags",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "owner_id"})} // ADMIN별 태그 이름 중복 방지
)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 태그 이름(ADMIN 별)
    @NotBlank(message = "태그 이름은 필수입니다.")
    @Size(max = 30, message = "태그 이름은 최대 30자까지 입력 가능합니다.")
    private String name;

    // 태그 설명 (선택)
    @Size(max = 255, message = "설명은 최대 255자까지 입력 가능합니다.")
    private String description;

    // 태그 생성자(ADMIN만 가능)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private User owner;

    // isDeleted로 soft-delete 가능
    private boolean isDeleted;
}
