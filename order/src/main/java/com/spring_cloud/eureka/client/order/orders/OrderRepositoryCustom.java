package com.spring_cloud.eureka.client.order.orders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
	Page<OrderResponse> searchOrders(OrderSearchRequest searchRequest, Pageable pageable, String role, String userId);
}
