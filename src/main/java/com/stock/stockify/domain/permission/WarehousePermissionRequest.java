package com.stock.stockify.domain.permission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WarehousePermissionRequest {
    private Long warehouseId;
    private boolean canManageInventory;
    private boolean canManageOrders;
    private boolean canViewReports;
}
