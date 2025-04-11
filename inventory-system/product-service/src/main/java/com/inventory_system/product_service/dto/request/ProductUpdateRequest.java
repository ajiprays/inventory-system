package com.inventory_system.product_service.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductUpdateRequest extends ProductCreateRequest {
	private Long id;

}