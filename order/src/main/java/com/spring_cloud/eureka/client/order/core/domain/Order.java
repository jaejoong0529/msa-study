package com.spring_cloud.eureka.client.order.core.domain;

import com.spring_cloud.common.exception.BusinessException;
import com.spring_cloud.common.exception.ErrorCode;
import com.spring_cloud.common.jpa.BaseTimeEntity;
import com.spring_cloud.eureka.client.order.core.enums.OrderStatus;
import com.spring_cloud.eureka.client.order.orders.OrderResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@ElementCollection
	@CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
	@Column(name = "order_item_id")
	private List<Long> orderItemIds;

	private String createdBy;
	private String updatedBy;
	private LocalDateTime deletedAt;
	private String deletedBy;

	@Builder(access = AccessLevel.PRIVATE)
	private Order(List<Long> orderItemIds, String createdBy) {
		this.orderItemIds = orderItemIds;
		this.createdBy = createdBy;
	}

	@PrePersist
	protected void orderPrePersist() {
		if (status == null) {
			status = OrderStatus.CREATED;
		}
	}

	public static Order create(List<Long> orderItemIds, String createdBy) {
		return Order.builder()
				.orderItemIds(orderItemIds)
				.createdBy(createdBy)
				.build();
	}

	public void updateDetails(List<Long> orderItemIds, String updatedBy, OrderStatus newStatus) {
		validateStatus(newStatus);
		this.orderItemIds = orderItemIds;
		this.updatedBy = updatedBy;
		this.status = newStatus;
	}

	public void markDeleted(String deletedBy) {
		this.deletedBy = deletedBy;
		this.deletedAt = LocalDateTime.now();
	}

	private void validateStatus(OrderStatus newStatus) {
		if (newStatus == null) {
			throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS);
		}
	}

	public OrderResponse toResponse() {
		return OrderResponse.from(this);
	}
}
