package com.inventory_system.analytics_service.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.inventory_system.analytics_service.persistence.entity.SalesRecord;

@Repository
public interface SalesRecordRepository extends JpaRepository<SalesRecord, Long> {

    @Query(value = """
		SELECT 
    		sr.product_id, 
    		SUM(sr.quantity_sold) AS total_sold
		FROM sales_records sr
		GROUP BY sr.product_id
		ORDER BY total_sold DESC
		LIMIT 10
    		""", nativeQuery = true)
    List<Object[]> findTopSellingProducts();
	    
}