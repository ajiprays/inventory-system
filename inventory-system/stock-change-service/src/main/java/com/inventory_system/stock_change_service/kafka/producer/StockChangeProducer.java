package com.inventory_system.stock_change_service.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory_system.stock_change_service.persistence.entity.StockChange;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@Service
@AllArgsConstructor
public class StockChangeProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;
	
	@SneakyThrows
	public void sendStockChange(StockChange stockChange) {
        kafkaTemplate.send(
        		"stock-changes", 
        		stockChange.getProductId().toString(), 
        		objectMapper.writeValueAsString(stockChange));
    }
	
}