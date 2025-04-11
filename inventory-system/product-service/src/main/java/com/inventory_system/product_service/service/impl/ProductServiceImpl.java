package com.inventory_system.product_service.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.inventory_system.product_service.dto.ProductDocument;
import com.inventory_system.product_service.dto.request.ProductCreateRequest;
import com.inventory_system.product_service.dto.request.ProductUpdateRequest;
import com.inventory_system.product_service.dto.request.UpdateStockRequest;
import com.inventory_system.product_service.dto.response.ProductResponse;
import com.inventory_system.product_service.kafka.producer.ProductEventProducer;
import com.inventory_system.product_service.persistence.entity.Product;
import com.inventory_system.product_service.persistence.repository.ProductRepository;
import com.inventory_system.product_service.persistence.repository.ProductSearchRepository;
import com.inventory_system.product_service.service.ProductService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
    private final ProductEventProducer productEventProducer;
    private final ProductSearchRepository productSearchRepository;
    
    
	private void validateRequest(ProductCreateRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        if (request.getMinStockThreshold() < 0) {
            throw new IllegalArgumentException("Minimum stock threshold cannot be negative");
        }
    }
	
	@Transactional
	@Override
	public ProductResponse add(ProductCreateRequest request) {
		validateRequest(request);
        Product product = mapToEntity(request);
        product = productRepository.save(product);
        
        ProductDocument productDocument = new ProductDocument();
        BeanUtils.copyProperties(product, productDocument);
        productSearchRepository.save(productDocument);
        
        // Publikasikan event
        productEventProducer.sendProductEvent(product, "PRODUCT_ADDED");

        // Mapping Entity ke Response DTO
        return mapToResponse(product);
	}

	@Transactional
	@Override
	public ProductResponse edit(ProductUpdateRequest request) {
		Product product = productRepository.findById(request.getId())
	            .orElseThrow(() -> new RuntimeException("Product not found"));

        validateRequest(request);
        mapToEntity(product, request);
        product = productRepository.save(product);
        
        ProductDocument productDocument = new ProductDocument();
        BeanUtils.copyProperties(product, productDocument);
        productSearchRepository.save(productDocument);
        
        productEventProducer.sendProductEvent(product, "PRODUCT_UPDATED");
        
        return mapToResponse(product);
	}

	@Transactional
	@Override
    public ProductResponse updateStock(UpdateStockRequest request) {
		Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
		if (request.getNewStock() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
		
		product.setStock(request.getNewStock());
        product = productRepository.save(product);
        
        ProductDocument productDocument = new ProductDocument();
        BeanUtils.copyProperties(product, productDocument);
        productSearchRepository.save(productDocument);
        
        productEventProducer.sendProductEvent(product, "PRODUCT_UPDATED");
        
        return mapToResponse(product);
    }
	
	@Transactional
	@Override
	public void delete(Long id) {
		Product product = productRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Product not found"));

        productRepository.deleteById(id);
        
        productSearchRepository.deleteById(id);
        
        productEventProducer.sendProductEvent(product, "PRODUCT_DELETED");
	}

	@Override
	public ProductResponse getById(Long id) {
		Product product = productRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponse(product);
	}

	@Override
	public List<ProductResponse> getAllProducts() {
		return productRepository.findAll().stream()
				.map(this::mapToResponse).toList();
	}
	
	@Override
	public List<ProductResponse> getAllProducts(String inquiry) {
		var res = new ArrayList<ProductResponse>();
		productSearchRepository.findAllByNameContainingOrCategoryContaining(inquiry, inquiry)
				.forEach(data -> res.add(mapToResponse(data)));
		return res;
	}
	
	
	private Product mapToEntity(ProductCreateRequest request) {
		Product product = new Product();
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setStock(0);
        product.setMinStockThreshold(request.getMinStockThreshold());
		return product;
	}
	
	private void mapToEntity(Product product, ProductUpdateRequest request) {
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setMinStockThreshold(request.getMinStockThreshold());
	}
	
	private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setVersion(product.getVersion());
        response.setName(product.getName());
        response.setCategory(product.getCategory());
        response.setStock(product.getStock());
        response.setMinStockThreshold(product.getMinStockThreshold());
        return response;
    }
	
	private ProductResponse mapToResponse(ProductDocument product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setCategory(product.getCategory());
        response.setStock(product.getStock());
        response.setMinStockThreshold(product.getMinStockThreshold());
        return response;
    }
}