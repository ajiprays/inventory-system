package com.inventory_system.stock_change_service.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory_system.stock_change_service.dto.event.ProductEvent;
import com.inventory_system.stock_change_service.persistence.entity.Product;
import com.inventory_system.stock_change_service.service.ProductCacheService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ProductEventConsumer {

	private final ProductCacheService productCacheService;
	private final ObjectMapper objectMapper;
	
	
    @KafkaListener(topics = "product-events", groupId = "stock-change-group")
    public void consume(ConsumerRecord<String, String> record) {
    	try {
	    	String message = record.value();
	    	ProductEvent event = objectMapper.readValue(message, ProductEvent.class);
	    	log.info("stock group product event : {}", event);
	    	if (event.getEventType().equals("PRODUCT_UPDATED") || event.getEventType().equals("PRODUCT_ADDED")) {
	            // Perbarui cache secara proaktif dengan data dari event
	            Product product = new Product();
	            product.setId(event.getProductId());
	            product.setName(event.getProductName());
	            product.setCategory(event.getCategory());
	            product.setStock(event.getStock());
	            product.setMinStockThreshold(event.getMinStockThreshold());
	            productCacheService.cacheProduct(event.getProductId(), product);
	        } else if (event.getEventType().equals("PRODUCT_DELETED")) {
	            // Invalidasi cache untuk produk yang dihapus
	            productCacheService.evictProductFromCache(event.getProductId());
	        }
    	} catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize ProductEvent: " + record.value(), e);
        }
    }
	
}