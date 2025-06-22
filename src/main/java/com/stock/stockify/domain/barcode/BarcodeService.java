package com.stock.stockify.domain.barcode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.stockify.domain.inventory.InventoryItem;
import com.stock.stockify.domain.inventory.InventoryItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class BarcodeService {

    private final InventoryItemRepository inventoryItemRepository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public BarcodeService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public InventoryItem processBarcode(String barcode) throws Exception {
        log.info("바코드 스캔 요청 수신: {}", barcode);

        Optional<InventoryItem> optionalItem = inventoryItemRepository.findByBarcodeId(barcode);

        if (optionalItem.isPresent()) {
            InventoryItem item = optionalItem.get();
            item.setQuantity(item.getQuantity() + 1);
            item.setUpdatedAt(LocalDateTime.now());
            inventoryItemRepository.save(item);

            log.info("기존 상품 수량 증가: {} (수량: {})", item.getName(), item.getQuantity());
            return item;
        } else {
            String url = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";
            String response = restTemplate.getForObject(url, String.class);

            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode productNode = jsonNode.get("product");

            if (productNode != null) {
                String name = productNode.has("product_name") ? productNode.get("product_name").asText() : "Unknown Product";
                String brand = productNode.has("brands") ? productNode.get("brands").asText() : "Unknown Brand";

                InventoryItem newItem = InventoryItem.builder()
                        .barcodeId(barcode)
                        .name(name + " - " + brand)
                        .quantity(1)
                        .price(0.0)
                        .warehouseId(1L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                inventoryItemRepository.save(newItem);

                log.info("신규 상품 추가됨: {}", newItem.getName());
                return newItem;
            } else {
                throw new Exception("API로부터 상품 정보를 불러올 수 없습니다.");
            }
        }
    }
}