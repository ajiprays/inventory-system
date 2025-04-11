package com.inventory_system.stock_change_service.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.inventory_system.stock_change_service.common.type.ChangeType;
import com.inventory_system.stock_change_service.dto.request.StockChangeCreateRequest;
import com.inventory_system.stock_change_service.dto.response.StockChangeResponse;
import com.inventory_system.stock_change_service.kafka.producer.StockChangeProducer;
import com.inventory_system.stock_change_service.persistence.entity.Product;
import com.inventory_system.stock_change_service.persistence.entity.StockChange;
import com.inventory_system.stock_change_service.persistence.repository.StockChangeRepository;
import com.inventory_system.stock_change_service.service.ProductCacheService;
import com.inventory_system.stock_change_service.service.RedisLockService;
import com.inventory_system.stock_change_service.service.StockChangeService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class StockChangeServiceImpl implements StockChangeService {

	private final StockChangeRepository stockChangeRepository;
    private final StockChangeProducer stockChangeProducer;
    private final RestTemplate restTemplate;
    private final RedisLockService redisLockService;
    private final ProductCacheService productCacheService;
    
    
    private void validateRequest(StockChangeCreateRequest request) {
        if (request.getProductId() == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (!request.getChangeType().equals(ChangeType.SALE) && !request.getChangeType().equals(ChangeType.RESTOCK)) {
            throw new IllegalArgumentException("Change type must be SALE or RESTOCK");
        }
    }
    
	@Transactional
	@Override
	public StockChangeResponse changeStock(StockChangeCreateRequest request) {
		validateRequest(request);

		// Ambil data produk dari cache atau Product Service
        Product product = getProductWithCache(request.getProductId());
        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        // Validasi ketersediaan stok untuk penjualan
        if (request.getChangeType().equals(ChangeType.SALE) && product.getStock() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        log.info("product stock : {}", product.getStock());
        // Hitung stok baru
        Integer newStock = request.getChangeType().equals(ChangeType.SALE)
            ? product.getStock() - request.getQuantity()
            : product.getStock() + request.getQuantity();

        // Perbarui stok di Product Service dengan Redis lock
        String lockKey = "product:stock:lock:" + request.getProductId();
        try {
            if (redisLockService.acquireLock(lockKey, 10_000)) { // Lock selama 10 detik
            	// Panggil endpoint Product Service untuk update stok dengan retry
                updateStockInProductService(request.getProductId(), newStock);
                // Cache akan diinvalidasi oleh ProductEventConsumer, jadi kita tidak perlu memperbarui cache di sini
            } else {
                throw new RuntimeException("Failed to acquire lock, please retry");
            }
        } finally {
            redisLockService.releaseLock(lockKey);
        }

        // Simpan histori stok
        StockChange stockChange = mapToEntity(request, newStock);
        stockChange = stockChangeRepository.save(stockChange);

        // Publikasikan event
        stockChangeProducer.sendStockChange(stockChange);

        return mapToResponse(stockChange);
	}

	private StockChange mapToEntity(StockChangeCreateRequest request, Integer newStock) {
		StockChange stockChange = new StockChange();
        stockChange.setProductId(request.getProductId());
        stockChange.setQuantity(request.getQuantity());
        stockChange.setChangeType(request.getChangeType());
        stockChange.setNewStock(newStock);
        stockChange.setTimestamp(LocalDateTime.now());
		return stockChange;
	}

	@Override
	public List<StockChangeResponse> getStockHistory(Long productId, LocalDateTime start, LocalDateTime end) {
		return stockChangeRepository.findAllByProductIdAndPeriod(productId, start, end)
				.stream().map(this::mapToResponse)
				.toList();
	}

	private Product getProductWithCache(Long productId) {
        // Coba ambil dari cache
        Product product = productCacheService.getProductFromCache(productId);
        log.info("product cache : {}", product);
        if (product != null) {
            return product;
        }

        // Jika tidak ada di cache, ambil dari Product Service dengan retry
        product = getProductFromProductService(productId);
        log.info("product from service : {}", product);
        if (product != null) {
            productCacheService.cacheProduct(productId, product);
        }
        return product;
    }
	
	@Retryable(
        value = { RestClientException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000) // Delay 1 detik antar percobaan
    )
    private Product getProductFromProductService(Long productId) {
        return restTemplate.getForObject(
            "http://product-service:8082/products/" + productId,
            Product.class
        );
    }

	@Retryable(
        value = { RestClientException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000) // Delay 1 detik antar percobaan
    )
    private void updateStockInProductService(Long productId, Integer newStock) {
        ResponseEntity<Product> response = restTemplate.exchange(
        	    "http://product-service:8082/products/stock",
        	    HttpMethod.PUT,
        	    new HttpEntity<>(new UpdateStockRequest(productId, newStock)),
        	    Product.class
        	);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to update stock in Product Service");
        }
    }

	private StockChangeResponse mapToResponse(StockChange stockChange) {
        StockChangeResponse response = new StockChangeResponse();
        response.setId(stockChange.getId());
        response.setProductId(stockChange.getProductId());
        response.setQuantity(stockChange.getQuantity());
        response.setChangeType(stockChange.getChangeType());
        response.setNewStock(stockChange.getNewStock());
        response.setTimestamp(stockChange.getTimestamp());
        return response;
    }

}

@Getter
@Setter
@AllArgsConstructor
class UpdateStockRequest {

	private Long id;
	private Integer newStock;

}