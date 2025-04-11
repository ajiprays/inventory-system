package com.inventory_system.analytics_service.service;

import java.util.List;

import com.inventory_system.analytics_service.dto.response.TopProductResponse;

public interface AnalyticsService {

	List<TopProductResponse> getTopSellingProducts();
	
}