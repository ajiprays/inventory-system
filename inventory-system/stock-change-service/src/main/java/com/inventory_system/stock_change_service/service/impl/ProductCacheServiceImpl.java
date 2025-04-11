package com.inventory_system.stock_change_service.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.inventory_system.stock_change_service.persistence.entity.Product;
import com.inventory_system.stock_change_service.service.ProductCacheService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductCacheServiceImpl implements ProductCacheService {

	private final RedisTemplate<String, Object> redisTemplate;
    private static final long CACHE_TTL_MINUTES = 10; // Cache berlaku selama 10 menit

    @Override
    public Product getProductFromCache(Long productId) {
        return (Product)redisTemplate.opsForValue().get("product:" + productId);
    }

    @Override
    public void cacheProduct(Long productId, Product product) {
        redisTemplate.opsForValue().set(
            "product:" + productId,
            product,
            CACHE_TTL_MINUTES,
            TimeUnit.MINUTES
        );
    }

    @Override
    public void evictProductFromCache(Long productId) {
        redisTemplate.delete("product:" + productId);
    }
	
}