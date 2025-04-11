package com.inventory_system.product_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventory_system.product_service.dto.request.ProductCreateRequest;
import com.inventory_system.product_service.dto.request.ProductUpdateRequest;
import com.inventory_system.product_service.dto.request.UpdateStockRequest;
import com.inventory_system.product_service.dto.response.ProductResponse;
import com.inventory_system.product_service.service.ProductService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductCreateRequest request) {
        ProductResponse response = productService.add(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        ProductResponse response = productService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestParam(required = false) String inquiry) {
    	if(!(inquiry != null && !inquiry.isBlank())) {
            List<ProductResponse> products = productService.getAllProducts();
            log.debug("fetch data from postgres");
            return ResponseEntity.ok(products);    		
    	}
        List<ProductResponse> products = productService.getAllProducts(inquiry);
        log.debug("fetch data from elasticsearch");
        return ResponseEntity.ok(products);
    }

    @PutMapping
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductUpdateRequest request) {
        ProductResponse response = productService.edit(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/stock")
    public ResponseEntity<ProductResponse> updateStock(@RequestBody UpdateStockRequest request) {
        ProductResponse response = productService.updateStock(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}