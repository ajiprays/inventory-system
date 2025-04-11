package com.inventory_system.stock_change_service.persistence.entity;

import lombok.Data;

@Data
public class Product {

    private Long id;
    private String name;
    private String category;
    private Integer stock;
    private Integer minStockThreshold;

}