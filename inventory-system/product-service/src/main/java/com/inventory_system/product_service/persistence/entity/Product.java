package com.inventory_system.product_service.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "products")
public class Product {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Long id;
	
	@Column(name = "name")
    private String name;
	
	@Column(name = "category")
    private String category;
	
	@Column(name = "stock")
    private Integer stock;
	
	@Column(name = "min_stock_threshold")
    private Integer minStockThreshold;

	@Version
	@Column(name = "version")
    private Long version;
	
}