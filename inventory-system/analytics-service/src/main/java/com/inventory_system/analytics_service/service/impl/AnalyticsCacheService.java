package com.inventory_system.analytics_service.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.inventory_system.analytics_service.dto.response.TopProductResponse;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AnalyticsCacheService {

	private final RedisTemplate<String, Object> redisTemplate;
    private static final long CACHE_TTL_MINUTES = 5; // Cache berlaku selama 5 menit
	private static final String KEY_CACHE_TOP_PRODUCTS = "top-products";
	
	
    public List<TopProductResponse> getTopProductsFromCache() {
        return (List<TopProductResponse>)redisTemplate.opsForValue().get(KEY_CACHE_TOP_PRODUCTS);
    }

    public void cacheTopProducts(List<TopProductResponse> topProducts) {
        redisTemplate.opsForValue().set(
            KEY_CACHE_TOP_PRODUCTS,
            topProducts,
            CACHE_TTL_MINUTES,
            TimeUnit.MINUTES
        );
    }

    public void evictTopProductsCache() {
        redisTemplate.delete(KEY_CACHE_TOP_PRODUCTS);
    }
}