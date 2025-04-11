package com.inventory_system.product_service.dto.event;

import lombok.Data;

@Data
public class ProductEvent {
	
	private Long productId;
    private String eventType; // PRODUCT_ADDED, PRODUCT_UPDATED, PRODUCT_DELETED
    private String productName;
    private String category;
    private Integer stock;
    private Integer minStockThreshold;
    
}