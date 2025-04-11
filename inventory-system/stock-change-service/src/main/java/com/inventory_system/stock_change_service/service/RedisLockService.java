package com.inventory_system.stock_change_service.service;

public interface RedisLockService {

	public boolean acquireLock(String lockKey, long timeoutMillis);
    public void releaseLock(String lockKey);
	
}