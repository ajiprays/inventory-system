package com.inventory_system.notification_service.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory_system.notification_service.dto.event.ProductEvent;
import com.inventory_system.notification_service.service.EmailService;

@Service
public class NotificationConsumer {

	private final EmailService emailService;
	private final ObjectMapper objectMapper;
	
	public NotificationConsumer(EmailService emailService, ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }
	
	@KafkaListener(topics = "product-events", groupId = "notification-group")
	public void consume(ConsumerRecord<String, String> record) {
		try {
			String message = record.value();
			ProductEvent productEvent = objectMapper.readValue(message, ProductEvent.class);
			if(productEvent.getEventType().equals("PRODUCT_UPDATED") && 
					(productEvent.getStock() <= productEvent.getMinStockThreshold())) {
				System.out.println("Low stock alert for product: " + productEvent.getProductName() +
		                ", Current stock: " + productEvent.getStock() +
		                ", Threshold: " + productEvent.getMinStockThreshold());
				
				emailService.sendLowStockEmail(productEvent);			
			}
		} catch (JsonProcessingException e) {
	        throw new RuntimeException("Failed to deserialize ProductEvent: " + record.value(), e);
	    }
	}

}