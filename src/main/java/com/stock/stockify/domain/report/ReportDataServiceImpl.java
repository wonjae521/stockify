package com.stock.stockify.domain.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.stock.stockify.domain.inventory.InventoryStatusLogRepository;
import com.stock.stockify.domain.order.OrderItem;
import com.stock.stockify.domain.order.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportDataServiceImpl implements ReportDataService {

    private final InventoryStatusLogRepository statusLogRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public String generateJsonStats(Long warehouseId, String periodStart, String periodEnd) {
        LocalDate start = LocalDate.parse(periodStart);
        LocalDate end = LocalDate.parse(periodEnd);

        // 총 입고 수량
        int totalIn = statusLogRepository.sumInboundQuantity(warehouseId, start, end);

        // 총 출고 수량
        int totalOut = statusLogRepository.sumOutboundQuantity(warehouseId, start, end);

        // 주문 항목 조회 (OrderItem → Order.warehouseId 기준)
        List<OrderItem> orderItems = orderItemRepository.findByCreatedAtBetweenAndItem_WarehouseId(
                start.atStartOfDay(), end.plusDays(1).atStartOfDay(), warehouseId);

        // JSON 구성
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.put("period", periodStart + " ~ " + periodEnd);
        root.put("total_inbound", totalIn);
        root.put("total_outbound", totalOut);
        root.put("sales_total", calculateSales(orderItems));

        ArrayNode orderArray = mapper.createArrayNode();
        for (OrderItem item : orderItems) {
            ObjectNode o = mapper.createObjectNode();
            o.put("item", item.getItem().getName());
            o.put("quantity", item.getQuantity());
            o.put("price", item.getItem().getPrice());
            o.put("date", item.getOrder().getCreatedAt().toLocalDate().toString());
            orderArray.add(o);
        }
        root.set("orders", orderArray);

        return root.toPrettyString();
    }

    private int calculateSales(List<OrderItem> items) {
        return items.stream()
                .filter(i -> i.getItem() != null && i.getItem().getPrice() != null)
                .mapToInt(i -> (int) Math.round(i.getItem().getPrice() * i.getQuantity()))
                .sum();
    }

}
