package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.user.*;
import com.stock.stockify.domain.warehouse.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;
    private final PasswordEncoder passwordEncoder;

    // 창고 생성
    @Transactional
    public Warehouse createWarehouse(String name, String description) {
        if (warehouseRepository.existsByName(name)) {
            throw new IllegalArgumentException("이미 존재하는 창고 이름입니다.");
        }
        return warehouseRepository.save(Warehouse.builder()
                .name(name)
                .description(description)
                .build());
    }

    // 사용자 등록 + 창고 권한 연결
    @Transactional
    public User registerUserWithWarehouse(String username, String password, String email, RoleType role, Long warehouseId) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new IllegalArgumentException("창고를 찾을 수 없습니다."));

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .roleType(role)
                .warehouse(warehouse)
                .build();

        return userRepository.save(user);
    }
}
