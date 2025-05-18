package com.stock.stockify.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * 주문(Order) 관련 API 요청을 처리하는 컨트롤러입니다.
 * - 주문 생성
 * - 주문 단건 조회
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문을 생성하는 API
     * POST /api/orders
     */
    @PostMapping
    public UUID createOrder(@RequestBody OrderRequestDto dto) {
        return orderService.createOrder(dto);
    }

    /**
     * 특정 주문을 조회하는 API
     * GET /api/orders/{id}
     */
    @GetMapping("/{id}")
    public OrderResponseDto getOrder(@PathVariable UUID id) {
        return orderService.getOrder(id);
    }
}
