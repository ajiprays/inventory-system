package com.inventory_system.analytics_service.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventory_system.analytics_service.persistence.entity.StockChange;

@Repository
public interface StockChangeRepository extends JpaRepository<StockChange, Long> {

}