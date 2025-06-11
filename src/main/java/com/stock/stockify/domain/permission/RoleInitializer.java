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
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        List<String> defaultRoles = List.of("ADMIN", "SUBADMIN", "STAFF");

        Map<String, Role> existingRoles = roleRepository.findAll().stream()
                .collect(Collectors.toMap(Role::getName, Function.identity()));

        List<Role> rolesToSave = defaultRoles.stream()
                .filter(name -> !existingRoles.containsKey(name))
                .map(name -> Role.builder().name(name).build())
                .toList();

        if (!rolesToSave.isEmpty()) {
            roleRepository.saveAll(rolesToSave);
            System.out.println("새 역할 " + rolesToSave.size() + "개 추가됨.");
        } else {
            System.out.println("모든 역할이 이미 등록되어 있습니다.");
        }
    }
}
