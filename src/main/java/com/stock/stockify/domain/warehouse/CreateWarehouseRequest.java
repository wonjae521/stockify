package com.stock.stockify.domain.warehouse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateWarehouseRequest {
    private String name;
    private String description;
}
