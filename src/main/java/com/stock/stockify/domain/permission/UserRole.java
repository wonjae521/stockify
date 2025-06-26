package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.warehouse.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * 사용자(User)가 특정 창고(Warehouse)에 대해 어떤 역할(직무)을 수행하는지 나타내는 엔티티
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_roles") // 테이블명 user_roles
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 역할을 가지는 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 역할이 적용되는 창고
    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;

    // 역할 이름: "ADMIN", "SUBADMIN", "STAFF"
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;
}
