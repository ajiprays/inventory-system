package com.inventory_system.product_service.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.inventory_system.product_service.service.RedisLockService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RedisLockServiceImpl implements RedisLockService {
	
	private final RedisTemplate<String, String> redisTemplate;
	
	
	@Override
	public boolean acquireLock(String lockKey, long timeoutMillis) {
		String lockValue = Thread.currentThread().getId() + ":" + System.currentTimeMillis();
        Boolean success = redisTemplate.opsForValue()
        		.setIfAbsent(lockKey, lockValue, timeoutMillis, TimeUnit.MILLISECONDS);
        return (success != null && success);
	}

	@Override
	public void releaseLock(String lockKey) {
		redisTemplate.delete(lockKey);
	}

	
}