package com.inventory_system.analytics_service.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory_system.analytics_service.dto.event.ProductEvent;
import com.inventory_system.analytics_service.persistence.entity.Product;
import com.inventory_system.analytics_service.persistence.repository.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductEventConsumer {

	private final ProductRepository productRepository;
	private final ObjectMapper objectMapper;
	
//    @KafkaListener(topics = "product-events", groupId = "analytics-group")
    public void consume(ConsumerRecord<String, String> record) {
    	try {
    		String message = record.value();
			ProductEvent event = objectMapper.readValue(message, ProductEvent.class);
	    	if (event.getEventType().equals("PRODUCT_UPDATED") || event.getEventType().equals("PRODUCT_ADDED")) {
	    		
	            Product product = new Product();
	            product.setId(event.getProductId());
	            product.setName(event.getProductName());
	            product.setCategory(event.getCategory());
	            productRepository.save(product);
	            
	        } else if (event.getEventType().equals("PRODUCT_DELETED")) {
	        	productRepository.deleteById(event.getProductId());
	        }
    	} catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize ProductEvent: " + record.value(), e);
        }
    }
	
}