package com.inventory_system.analytics_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.inventory_system.analytics_service.dto.response.TopProductResponse;
import com.inventory_system.analytics_service.persistence.entity.Product;
import com.inventory_system.analytics_service.persistence.repository.ProductRepository;
import com.inventory_system.analytics_service.persistence.repository.SalesRecordRepository;
import com.inventory_system.analytics_service.service.AnalyticsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
	
	private final SalesRecordRepository salesRecordRepository;
    private final ProductRepository productRepository;
    private final AnalyticsCacheService analyticsCacheService;
    
    
    @Override
    public List<TopProductResponse> getTopSellingProducts() {
        // Coba ambil dari cache
        List<TopProductResponse> cachedResult = analyticsCacheService.getTopProductsFromCache();
        if (cachedResult != null) {
            return cachedResult;
        }

        // Jika tidak ada di cache, hitung dari database
        List<Object[]> topProducts = salesRecordRepository.findTopSellingProducts();
        List<TopProductResponse> result = topProducts.stream()
            .map(data -> {
                Long productId = (Long) data[0];
                Long totalSold = (Long) data[1];
                Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
                TopProductResponse dto = new TopProductResponse();
                dto.setProductName(product.getName());
                dto.setTotalSold(totalSold);
                return dto;
            }).toList();

        // Simpan ke cache
        analyticsCacheService.cacheTopProducts(result);
        return result;
    }
    
}