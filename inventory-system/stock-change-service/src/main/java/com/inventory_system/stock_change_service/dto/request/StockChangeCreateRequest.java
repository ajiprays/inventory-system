package com.inventory_system.stock_change_service.dto.request;

import com.inventory_system.stock_change_service.common.type.ChangeType;

import lombok.Data;

@Data
public class StockChangeCreateRequest {

	private Long productId;
    private Integer quantity;
    private ChangeType changeType;

}