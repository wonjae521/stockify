package com.stock.stockify.domain.permission;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {
        // 초기 권한 목록
        List<String> defaultPermissions = List.of(
                "INVENTORY_READ", "INVENTORY_WRITE", "INVENTORY_DELETE", "INVENTORY_VIEW",
                "ORDER_MANAGE", "REPORT_MANAGE", "REPORT_VIEW",
                "TAG_MANAGE", "NOTIFICATION_MANAGE", "NOTIFICATION_VIEW",
                "USER_MANAGE", "STORAGE_RETRIEVAL_MANAGE", "WAREHOUSE_MANAGE", "WAREHOUSE_VIEW"
        );

        // 모든 권한을 한 번에 가져와 Map으로 변환 (이름 → Permission)
        Map<String, Permission> existingPermissions = permissionRepository.findAll().stream()
                .collect(Collectors.toMap(Permission::getName, Function.identity()));

        // 아직 존재하지 않는 권한만 필터링
        List<Permission> permissionsToSave = defaultPermissions.stream()
                .filter(name -> !existingPermissions.containsKey(name))
                .map(name -> Permission.builder().name(name).build())
                .toList();

        // 새 권한 저장
        if (!permissionsToSave.isEmpty()) {
            permissionRepository.saveAll(permissionsToSave);
            System.out.println("새 권한 " + permissionsToSave.size() + "개 추가됨.");
        } else {
            System.out.println("모든 권한이 이미 등록되어 있습니다.");
        }
    }
}