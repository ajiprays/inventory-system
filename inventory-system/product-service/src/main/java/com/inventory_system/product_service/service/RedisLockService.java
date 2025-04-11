package com.inventory_system.product_service.service;

public interface RedisLockService {

	boolean acquireLock(String lockKey, long timeoutMillis);
    void releaseLock(String lockKey);
	
}