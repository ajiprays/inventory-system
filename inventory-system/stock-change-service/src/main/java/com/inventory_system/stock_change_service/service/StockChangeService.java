package com.inventory_system.stock_change_service.service;

import java.time.LocalDateTime;
import java.util.List;

import com.inventory_system.stock_change_service.dto.request.StockChangeCreateRequest;
import com.inventory_system.stock_change_service.dto.response.StockChangeResponse;

public interface StockChangeService {

	StockChangeResponse changeStock(StockChangeCreateRequest request);
	
	List<StockChangeResponse> getStockHistory(Long productId, 
			LocalDateTime start, LocalDateTime end);
	
}