package com.stock.stockify.domain.order;

import com.stock.stockify.domain.inventory.InventoryItem;
import com.stock.stockify.domain.inventory.InventoryItemRepository;
import com.stock.stockify.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.stock.stockify.domain.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 주문 생성 및 조회에 대한 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryItemRepository itemRepository;
    private final UserRepository userRepository;

    /**
     * 주문을 생성하는 메서드입니다.
     * - 사용자와 재고 품목 정보를 검증하고, 주문 및 주문 품목을 생성하여 저장합니다.X
     *
     * @param dto 주문 요청 정보
     * @return 생성된 주문 ID
     */
    @Transactional
    public int createOrder(OrderRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new IllegalArgumentException("Invalid user"));

        Order order = Order.builder()
            .user(user)
            .note(dto.getNote())
            .build();

        // 주문 품목 목록 생성
        List<OrderItem> orderItems = dto.getItems().stream().map(itemDto -> {
            InventoryItem item = itemRepository.findById(itemDto.getItemId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid item"));

            return OrderItem.builder()
                .order(order)
                .item(item)
                .quantity(itemDto.getQuantity())
                .build();
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 특정 주문 정보를 조회하는 메서드입니다.
     *
     * @param orderId 조회할 주문 ID
     * @return 주문 상세 정보를 담은 DTO
     */
    public OrderResponseDto getOrder(long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NoSuchElementException("Order not found"));

        return OrderResponseDto.builder()
            .orderId(order.getId())
            .userId(order.getUser().getId())
            .orderDate(order.getOrderDate())
            .status(order.getStatus().name())
            .note(order.getNote())
            .items(order.getOrderItems().stream().map(item ->
                new OrderResponseDto.ItemDetail(
                    item.getItem().getId(),
                    item.getItem().getName(),
                    item.getQuantity()
                )
            ).toList())
            .build();
    }
}
