package com.inventory_system.analytics_service.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory_system.analytics_service.persistence.entity.SalesRecord;
import com.inventory_system.analytics_service.persistence.entity.StockChange;
import com.inventory_system.analytics_service.persistence.repository.SalesRecordRepository;
import com.inventory_system.analytics_service.service.impl.AnalyticsCacheService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StockChangeConsumer {

	private final SalesRecordRepository salesRecordRepository;
    private final AnalyticsCacheService analyticsCacheService;
    private final ObjectMapper objectMapper;
    

    @KafkaListener(topics = "stock-changes", groupId = "analytics-group")
    public void consume(ConsumerRecord<String, String> record) {
    	try {
	    	String message = record.value();
	    	StockChange stockChange = objectMapper.readValue(message, StockChange.class);
	        if (stockChange.getChangeType().equals("SALE")) {
	            // Simpan catatan penjualan
	            SalesRecord salesRecord = new SalesRecord();
	            salesRecord.setProductId(stockChange.getProductId());
	            salesRecord.setQuantitySold(stockChange.getQuantity());
	            salesRecord.setTimestamp(stockChange.getTimestamp());
	            salesRecordRepository.save(salesRecord);
	
	            // Invalidasi cache karena data penjualan berubah
	            analyticsCacheService.evictTopProductsCache();
	        }
    	} catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize StockChange: " + record.value(), e);
        }
    }
	
}