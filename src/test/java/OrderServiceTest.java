import com.stock.stockify.domain.inventory.InventoryItem;
import com.stock.stockify.domain.inventory.InventoryItemRepository;
import com.stock.stockify.domain.order.OrderRequestDto;
import com.stock.stockify.domain.order.OrderService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = com.stock.stockify.StockifyApplication.class)
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryItemRepository itemRepository;

    @Test
    void 주문_디버깅_테스트() {
        // given: DB에 실제 존재하는 아이템 저장
        InventoryItem item = itemRepository.save(
                InventoryItem.builder()
                        .name("디버그 테스트 상품")
                        .price(12345.0)
                        .quantity(50)
                        .build()
        );

        Long itemId = item.getId();
        System.out.println("🔍 테스트용 아이템 ID: " + itemId);

        OrderRequestDto.OrderItemDto itemDto = new OrderRequestDto.OrderItemDto(itemId, 2);
        OrderRequestDto request = new OrderRequestDto(1L, "010-0000-0000", List.of(itemDto));

        // when: 주문 생성 (내부에서 item 조회 로그 출력됨)
        orderService.createOrder(request);

        // then: 예외 없이 완료되면 성공
    }
}
