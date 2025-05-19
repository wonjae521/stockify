package com.stock.stockify.domain.order;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {

    private Long userId;
    private String note;
    private List<OrderItemDto> items;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDto {
        private Long itemId;
        private int quantity;
    }
}
