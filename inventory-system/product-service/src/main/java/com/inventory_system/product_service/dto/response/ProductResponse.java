package com.inventory_system.product_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

	private Long id;
    private String name;
    private String category;
    private Integer stock;
    private Integer minStockThreshold;
    private Long version;
	
}