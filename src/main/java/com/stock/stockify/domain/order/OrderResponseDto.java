package com.stock.stockify.domain.order;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long orderId;
    private String customerName;
    private String customerPhone;
    private String status;
    private LocalDateTime createdAt;
    private List<ItemDetail> items;

    @Getter @Setter
    @AllArgsConstructor
    public static class ItemDetail {
        private Long itemId;
        private String itemName;
        private int quantity;
        private double priceAtOrder;
    }
}
