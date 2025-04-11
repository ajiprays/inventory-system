package com.inventory_system.notification_service.persistence.entity;

public class Product {

	private Long id;
    private String name;
    private String category;
    private Integer stock;
    private Integer minStockThreshold;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Integer getMinStockThreshold() {
		return minStockThreshold;
	}
	public void setMinStockThreshold(Integer minStockThreshold) {
		this.minStockThreshold = minStockThreshold;
	}
    
}