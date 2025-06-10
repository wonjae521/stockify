package com.stock.stockify.domain.permission;

import com.stock.stockify.domain.user.*;
import com.stock.stockify.domain.warehouse.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public UserResponseDto registerUserWithWarehouse(String username, String password, String email, RoleType role, List<Long> warehouseIds) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        User user = userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .roleType(role)
                .userWarehouseRoles(new ArrayList<>())
                //.warehouse(warehouse)
                .build());

        List<UserWarehouseRole> roles = new ArrayList<>();
        for (Long warehouseId : warehouseIds) {
            Warehouse warehouse = warehouseRepository.findById(warehouseId)
                    .orElseThrow(() -> new IllegalArgumentException("없는 창고입니다."));

            roles.add(UserWarehouseRole.builder()
                    .user(user)
                    .warehouse(warehouse)
                    .roleType(role)
                    .build());
        }

        // 저장
        List<Long> warehouseIdsResult = roles.stream()
                .map(r -> r.getWarehouse().getId())
                .toList();

        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRoleType(), warehouseIdsResult);
    }
}
