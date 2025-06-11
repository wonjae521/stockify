package com.stock.stockify.domain.user;

import com.stock.stockify.domain.permission.Role;
import com.stock.stockify.domain.permission.RoleRepository;
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
    private final RoleRepository roleRepository;

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
    public UserResponseDto registerUserWithWarehouse(String username, String password, String email, Long roleId, List<Long> warehouseIds) {

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("역할이 존재하지 않습니다."));

        User user = userRepository.save(User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .role(role)
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
                    .role(role)
                    .build());
        }

        // 저장
        List<Long> warehouseIdsResult = roles.stream()
                .map(r -> r.getWarehouse().getId())
                .toList();

        List<String> roless = user.getUserWarehouseRoles().stream()
                .map(uwr -> uwr.getRole().getName())
                .distinct() // 같은 역할 중복 제거 (선택)
                .toList();


        return new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), roless, warehouseIdsResult);
    }
}
