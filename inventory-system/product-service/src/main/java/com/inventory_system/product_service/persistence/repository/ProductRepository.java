package com.inventory_system.product_service.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventory_system.product_service.persistence.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}