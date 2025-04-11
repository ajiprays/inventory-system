package com.inventory_system.analytics_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory_system.analytics_service.dto.response.TopProductResponse;
import com.inventory_system.analytics_service.service.AnalyticsService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/analytics/top-selling-products")
@AllArgsConstructor
public class AnalyticsController {

	private final AnalyticsService analyticsService;

    @GetMapping
    public ResponseEntity<List<TopProductResponse>> getTopSellingProducts() {
        List<TopProductResponse> products = analyticsService.getTopSellingProducts();
        return ResponseEntity.ok(products);
    }
   
}