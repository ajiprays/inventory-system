package com.inventory_system.stock_change_service.dto.response;

import java.time.LocalDateTime;

import com.inventory_system.stock_change_service.common.type.ChangeType;

import lombok.Data;

@Data
public class StockChangeResponse {

	private Long id;
    private Long productId;
    private Integer quantity;
    private ChangeType changeType;
    private Integer newStock;
    private LocalDateTime timestamp;
    
}