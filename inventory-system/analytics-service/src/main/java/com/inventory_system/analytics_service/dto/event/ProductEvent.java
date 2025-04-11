package com.inventory_system.analytics_service.dto.event;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductEvent {
    private Long productId;
    private String eventType; // PRODUCT_ADDED, PRODUCT_UPDATED, PRODUCT_DELETED
    private String productName;
    private String category;
    private int stock;
    private int minStockThreshold;
}