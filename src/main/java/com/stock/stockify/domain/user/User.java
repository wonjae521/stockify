package com.stock.stockify.domain.user;

import com.stock.stockify.domain.warehouse.UserWarehouseRole;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// 데이터베이스 users 테이블과 매핑
// 회원의 아이디, 비밀번호, 역할, 가입일 등을 관리
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 고유 ID (PK)

    @Column(nullable = false, unique = true, length = 100)
    private String username; // 로그인용 아이디

    @Column(nullable = false)
    private String password; // 비밀번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User admin; // 사용자를 등록한 관리자


    /**
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType roleType; // 사용자 역할 (ADMIN, SUBADMIN, STAFF)
     */

    /**
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = true)
    private Warehouse warehouse;  // 사용자가 소속된 창고
     */

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserWarehouseRole> userWarehouseRoles = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String email; // 이메일

    @Column(nullable = false)
    private boolean emailVerified = false; // 이메일 인증 여부

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 가입 일자

    @PrePersist
    protected void onCreate() { // DB에 현재 시간 저장
        this.createdAt = LocalDateTime.now();
    } // 가입 시간

    public User getAdminOwner() {
        return (this.admin != null) ? this.admin : this;
    }

}
