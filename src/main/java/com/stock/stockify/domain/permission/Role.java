
package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 역할 이름: 예) "STAFF", "SUBADMIN", "TASKMASTER"
    @Column(nullable = false)
    private String name;

    // 이 역할을 만든 관리자 (ADMIN)
    @ManyToOne(fetch = FetchType.LAZY)
    private User admin;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RolePermission> rolePermissions = new ArrayList<>();
}
