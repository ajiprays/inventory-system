package com.inventory_system.stock_change_service.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inventory_system.stock_change_service.persistence.entity.StockChange;

@Repository
public interface StockChangeRepository extends JpaRepository<StockChange, Long> {

	@Query(value = """
		SELECT sc.*
		FROM stock_changes sc
		WHERE sc.product_id = :productId 
		  AND sc."timestamp" >= :start
		  AND sc."timestamp" <= :end 
		ORDER BY sc."timestamp" DESC
			""", nativeQuery = true)
	List<StockChange> findAllByProductIdAndPeriod(
			@Param(value = "productId") Long productId, 
			@Param(value = "start") LocalDateTime start, 
			@Param(value = "end") LocalDateTime end);

}