package com.inventory_system.notification_service.service;

import com.inventory_system.notification_service.dto.event.ProductEvent;

public interface EmailService {

	void sendLowStockEmail(ProductEvent product);
	
}