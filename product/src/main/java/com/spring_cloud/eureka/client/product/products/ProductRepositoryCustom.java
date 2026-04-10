package com.spring_cloud.eureka.client.product.products;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
	Page<ProductResponse> searchProducts(ProductSearchRequest searchRequest, Pageable pageable);
}
