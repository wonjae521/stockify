import com.stock.stockify.domain.inventory.InventoryItem;
import com.stock.stockify.domain.inventory.InventoryItemRepository;
import com.stock.stockify.domain.order.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    private OrderRepository orderRepository;
    private InventoryItemRepository itemRepository;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        itemRepository = mock(InventoryItemRepository.class);
        orderService = new OrderService(orderRepository, itemRepository);
    }

    @Test
    void 주문_정상_생성() {
        // given
        Long itemId = 1L;
        InventoryItem item = InventoryItem.builder()
                .id(itemId)
                .name("테스트 상품")
                .price(1500.0)
                .build();

        OrderRequestDto.OrderItemDto orderItemDto = new OrderRequestDto.OrderItemDto(itemId, 3);
        OrderRequestDto request = new OrderRequestDto("홍길동", "010-1234-5678", List.of(orderItemDto));

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // when
        orderService.createOrder(request);

        // then
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(captor.capture());

        Order savedOrder = captor.getValue();
        assertThat(savedOrder.getCustomerName()).isEqualTo("홍길동");
        assertThat(savedOrder.getOrderItems()).hasSize(1);
        assertThat(savedOrder.getOrderItems().get(0).getItem().getName()).isEqualTo("테스트 상품");
        assertThat(savedOrder.getOrderItems().get(0).getPriceAtOrder()).isEqualTo(1500.0);
    }
    @Test
    void 주문을_id로_조회한다() {
        // given
        InventoryItem item = InventoryItem.builder()
                .id(1L)
                .name("테스트 상품")
                .price(1000.0)
                .build();

        OrderItem orderItem = OrderItem.builder()
                .id(10L)
                .item(item)
                .quantity(2)
                .priceAtOrder(1000.0)
                .build();

        Order order = Order.builder()
                .id(5L)
                .customerName("홍길동")
                .customerPhone("010-1111-2222")
                .status(OrderStatus.PROCESSING)
                .orderItems(List.of(orderItem))
                .createdAt(LocalDateTime.now())
                .build();

        orderItem.setOrder(order);

        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));

        // when
        OrderResponseDto response = orderService.getOrder(5L);

        // then
        assertThat(response.getOrderId()).isEqualTo(5L);
        assertThat(response.getCustomerName()).isEqualTo("홍길동");
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().get(0).getItemName()).isEqualTo("테스트 상품");
    }
    @Test
    void 존재하지_않는_품목_ID로_주문_생성하면_예외가_발생한다() {
        // given
        Long invalidItemId = 999L;
        OrderRequestDto.OrderItemDto invalidItem = new OrderRequestDto.OrderItemDto(invalidItemId, 2);
        OrderRequestDto request = new OrderRequestDto("고객A", "010-9999-9999", List.of(invalidItem));

        when(itemRepository.findById(invalidItemId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid item ID");
    }
    @Test
    void 품목_수량이_0_이하이면_예외가_발생한다() {
        // given
        Long itemId = 1L;
        InventoryItem item = InventoryItem.builder()
                .id(itemId)
                .name("샘플 품목")
                .price(1200.0)
                .build();

        OrderRequestDto.OrderItemDto itemDto = new OrderRequestDto.OrderItemDto(itemId, 0); // 0 이하 수량
        OrderRequestDto request = new OrderRequestDto("고객B", "010-0000-0000", List.of(itemDto));

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("0 보다 많은 수량을 입력해야 합니다.");
    }

}



