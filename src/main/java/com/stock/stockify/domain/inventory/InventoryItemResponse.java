package com.stock.stockify.domain.inventory;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class InventoryItemResponse {

    private Long id;
    private String name;
    private String category;
    private int quantity;
    private String unit;
    private Integer price;
    private String memo;

    private List<InventoryUnitResponse> units;

    public static InventoryItemResponse from(InventoryItem item) {
        return InventoryItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .category(item.getCategory().getName())
                .quantity(item.getQuantity())
                .unit(item.getUnit())
                .price(item.getPrice())
                .memo(item.getMemo())
                .units(item.getUnits().stream()
                        .map(InventoryUnitResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}
