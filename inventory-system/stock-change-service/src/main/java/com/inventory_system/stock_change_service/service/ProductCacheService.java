package com.inventory_system.stock_change_service.service;

import com.inventory_system.stock_change_service.persistence.entity.Product;

public interface ProductCacheService {

	Product getProductFromCache(Long productId);
	void cacheProduct(Long productId, Product product);
	void evictProductFromCache(Long productId);
	
}