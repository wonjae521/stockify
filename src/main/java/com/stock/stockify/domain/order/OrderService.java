package com.stock.stockify.domain.order;

import com.stock.stockify.domain.inventory.InventoryItem;
import com.stock.stockify.domain.inventory.InventoryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryItemRepository itemRepository;

    @Transactional
    public Long createOrder(OrderRequestDto dto) {
        if (itemRepository.count() == 0) {
            throw new IllegalStateException("주문을 생성하려면 먼저 재고를 등록해야 합니다.");
        }

        Order order = Order.builder()
                .customerName(dto.getCustomerName())
                .customerPhone(dto.getCustomerPhone())
                .build();

        List<OrderItem> orderItems = dto.getItems().stream().map(itemDto -> {
            if (itemDto.getQuantity() <= 0) {
                throw new IllegalArgumentException("0 보다 많은 수량을 입력해야 합니다.");
            }
            System.out.println("주문 요청된 item ID: " + itemDto.getItemId());
            InventoryItem item = itemRepository.findById(itemDto.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + itemDto.getItemId()));

            return OrderItem.builder()
                    .order(order)
                    .item(item)
                    .quantity(itemDto.getQuantity())
                    .priceAtOrder(item.getPrice())
                    .build();
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        List<OrderResponseDto.ItemDetail> itemDetails = order.getOrderItems().stream().map(orderItem ->
                new OrderResponseDto.ItemDetail(
                        orderItem.getItem().getId(),
                        orderItem.getItem().getName(),
                        orderItem.getQuantity(),
                        orderItem.getPriceAtOrder()
                )
        ).collect(Collectors.toList());

        return OrderResponseDto.builder()
                .orderId(order.getId())
                .customerName(order.getCustomerName())
                .customerPhone(order.getCustomerPhone())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .items(itemDetails)
                .build();
    }
}
