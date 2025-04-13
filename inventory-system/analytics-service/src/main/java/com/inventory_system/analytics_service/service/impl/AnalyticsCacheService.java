package com.inventory_system.analytics_service.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory_system.analytics_service.dto.response.TopProductResponse;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@Service
@AllArgsConstructor
public class AnalyticsCacheService {

	private final RedisTemplate<String, String> redisTemplate;
    private static final long CACHE_TTL_MINUTES = 5; // Cache berlaku selama 5 menit
	private static final String KEY_CACHE_TOP_PRODUCTS = "top-products";
	private final ObjectMapper objectMapper;
	
	@SneakyThrows
    public List<TopProductResponse> getTopProductsFromCache() {
    	String data = redisTemplate.opsForValue().get(KEY_CACHE_TOP_PRODUCTS);
    	if(data == null) return null;
    	return objectMapper.readValue(data, new TypeReference<List<TopProductResponse>>() {});
    }

    @SneakyThrows
    public void cacheTopProducts(List<TopProductResponse> topProducts) {
    	String data = objectMapper.writeValueAsString(topProducts);
        redisTemplate.opsForValue().set(
            KEY_CACHE_TOP_PRODUCTS,
            data,
            CACHE_TTL_MINUTES,
            TimeUnit.MINUTES
        );
    }

    public void evictTopProductsCache() {
        redisTemplate.delete(KEY_CACHE_TOP_PRODUCTS);
    }
}