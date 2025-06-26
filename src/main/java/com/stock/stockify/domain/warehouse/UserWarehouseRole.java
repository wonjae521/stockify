package com.stock.stockify.domain.warehouse;

import com.stock.stockify.domain.permission.Role;
import com.stock.stockify.domain.permission.UserRole;
import com.stock.stockify.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class UserWarehouseRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_name", nullable = false)
    private Role role;


    /**
    @Enumerated(EnumType.STRING)
    private RoleType roleType; // 예: ADMIN, SUBADMIN, STAFF
    */
}
