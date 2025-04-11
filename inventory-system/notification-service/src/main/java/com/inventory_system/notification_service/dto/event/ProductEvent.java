package com.inventory_system.notification_service.dto.event;

public class ProductEvent {
    private Long productId;
    private String eventType; // PRODUCT_ADDED, PRODUCT_UPDATED, PRODUCT_DELETED
    private String productName;
    private String category;
    private Integer stock;
    private Integer minStockThreshold;
    
    public ProductEvent(){
    	
    }
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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