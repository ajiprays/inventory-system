package com.inventory_system.product_service.persistence.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.inventory_system.product_service.dto.ProductDocument;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {

    Iterable<ProductDocument> findAllByNameContainingOrCategoryContaining(String name, String category);

}