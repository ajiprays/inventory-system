package com.inventory_system.analytics_service.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TopProductResponse {
    private String productName;
    private long totalSold;
}