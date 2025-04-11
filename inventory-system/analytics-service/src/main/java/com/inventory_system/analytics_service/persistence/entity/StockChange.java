package com.inventory_system.analytics_service.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "stock_changes")
@Data
public class StockChange {
	@Id
	@Column(name = "id")
    private Long id;
	
	@Column(name = "product_id")
    private Long productId;
	
	@Column(name = "quantity")
    private Integer quantity;
	
	@Column(name = "change_type")
    private String changeType; // RESTOCK, SALE
	
	@Column(name = "new_stock")
    private Integer newStock;
	
	@Column(name = "timestamp")
    private LocalDateTime timestamp;
}