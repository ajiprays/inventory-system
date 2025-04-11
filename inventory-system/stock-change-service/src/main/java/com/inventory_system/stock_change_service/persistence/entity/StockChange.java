package com.inventory_system.stock_change_service.persistence.entity;

import java.time.LocalDateTime;

import com.inventory_system.stock_change_service.common.type.ChangeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "stock_changes")
public class StockChange {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "product_id")
    private Long productId;
	
	@Column(name = "quantity")
    private Integer quantity;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "change_type")
    private ChangeType changeType;
	
	@Column(name = "new_stock")
	private Integer newStock;
	
	@Column(name = "timestamp")
    private LocalDateTime timestamp;
    
}