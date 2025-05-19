package com.stock.stockify;

import com.stock.stockify.domain.user.RoleType;
import com.stock.stockify.domain.user.User;
import com.stock.stockify.domain.user.UserRepository;
import com.stock.stockify.domain.warehouse.Warehouse;
import com.stock.stockify.domain.warehouse.WarehouseRepository;
import com.stock.stockify.global.util.JwtUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InventoryControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private WarehouseRepository warehouseRepository;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private EntityManager entityManager;

    @Test
    public void subadmin_can_register_inventory_in_own_warehouse() throws Exception {
        // given
        Warehouse warehouse = warehouseRepository.save(
                Warehouse.builder()
                        .name("A창고_" + UUID.randomUUID().toString().substring(0, 8))
                        .description("테스트용 창고")
                        .build()
        );

        User subadmin = userRepository.save(User.builder()
                .username("subadmin-test")
                .password("password")
                .email("subadmin-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com")
                .roleType(RoleType.SUBADMIN)
                .emailVerified(true)
                .warehouse(warehouse)
                .build()
        );

        entityManager.flush();
        entityManager.clear();

        User reloaded = userRepository.findByUsername("subadmin-test").orElseThrow();
        String token = jwtUtil.generateToken(reloaded.getUsername(), reloaded.getRoleType().name());

        System.out.println("📌 인증된 사용자 = " + reloaded.getUsername());
        System.out.println("📌 사용자 역할 = " + reloaded.getRoleType());
        System.out.println("📌 사용자 창고 ID = " + (reloaded.getWarehouse() != null ? reloaded.getWarehouse().getId() : "[null]"));

        // when + then
        mockMvc.perform(post("/api/inventories/warehouses/" + warehouse.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "testItem",
                      "quantity": 30,
                      "unit": "개"
                    }
                """))
                .andExpect(status().isOk());
    }
}
