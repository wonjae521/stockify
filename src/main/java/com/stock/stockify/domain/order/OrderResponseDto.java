package com.stock.stockify.domain.order;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private int orderId;
    private Long userId;
    private LocalDateTime orderDate;
    private String status;
    private String note;
    private List<ItemDetail> items;

    @Getter @Setter
    @AllArgsConstructor
    public static class ItemDetail {
        private Long itemId;
        private String itemName;
        private int quantity;
    }
}
