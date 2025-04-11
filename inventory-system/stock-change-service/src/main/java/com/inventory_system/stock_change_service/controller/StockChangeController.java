package com.inventory_system.stock_change_service.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventory_system.stock_change_service.dto.request.StockChangeCreateRequest;
import com.inventory_system.stock_change_service.dto.response.StockChangeResponse;
import com.inventory_system.stock_change_service.service.StockChangeService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/stock")
public class StockChangeController {
	
    private final StockChangeService stockChangeService;

    @PostMapping("/change")
    public ResponseEntity<StockChangeResponse> changeStock(@RequestBody StockChangeCreateRequest request) {
        StockChangeResponse response = stockChangeService.changeStock(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{productId}")
    public ResponseEntity<List<StockChangeResponse>> getStockHistory(
            @PathVariable Long productId,
            @RequestParam String start,
            @RequestParam String end) {
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        List<StockChangeResponse> history = stockChangeService.getStockHistory(productId, startDate, endDate);
        return ResponseEntity.ok(history);
    }
}