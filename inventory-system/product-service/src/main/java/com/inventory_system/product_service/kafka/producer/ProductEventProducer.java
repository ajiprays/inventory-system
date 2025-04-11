package com.inventory_system.product_service.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory_system.product_service.dto.event.ProductEvent;
import com.inventory_system.product_service.persistence.entity.Product;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ProductEventProducer {
	
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;
	
	@SneakyThrows
	public void sendProductEvent(Product product, String eventType) {
		ProductEvent event = new ProductEvent();
        event.setProductId(product.getId());
        event.setEventType(eventType);
        event.setProductName(product.getName());
        event.setCategory(product.getCategory());
        event.setStock(product.getStock());
        event.setMinStockThreshold(product.getMinStockThreshold());
        log.info("send product event : {}", product);
        kafkaTemplate.send(
        		"product-events", 
        		event.getProductId().toString(), 
        		objectMapper.writeValueAsString(event));
	}
	
}