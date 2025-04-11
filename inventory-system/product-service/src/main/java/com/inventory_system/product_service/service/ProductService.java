package com.inventory_system.product_service.service;

import java.util.List;

import com.inventory_system.product_service.dto.request.ProductCreateRequest;
import com.inventory_system.product_service.dto.request.ProductUpdateRequest;
import com.inventory_system.product_service.dto.request.UpdateStockRequest;
import com.inventory_system.product_service.dto.response.ProductResponse;

public interface ProductService {

	ProductResponse add(ProductCreateRequest request);
	ProductResponse edit(ProductUpdateRequest request);
	ProductResponse updateStock(UpdateStockRequest request);
	void delete(Long id);
	
	ProductResponse getById(Long id);
	List<ProductResponse> getAllProducts();
	List<ProductResponse> getAllProducts(String inquiry);
	
}