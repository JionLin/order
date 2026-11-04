package com.example.orderdemo.order.dto;

import com.example.orderdemo.order.domain.OrderEntity;
import com.example.orderdemo.order.domain.OrderStatus;
import java.time.Instant;

/**
 * Order API response.
 *
 * @author jiaolin
 */
public class OrderResponse {

    private final Long id;
    private final String skuId;
    private final Integer quantity;
    private final OrderStatus status;
    private final Instant createdAt;

    public OrderResponse(Long id, String skuId, Integer quantity, OrderStatus status, Instant createdAt) {
        this.id = id;
        this.skuId = skuId;
        this.quantity = quantity;
        this.status = status;
        this.createdAt = createdAt;
    }

    /**
     * Map entity to response.
     *
     * @param entity order entity
     * @return response DTO
     */
    public static OrderResponse from(OrderEntity entity) {
        return new OrderResponse(
                entity.getId(),
                entity.getSkuId(),
                entity.getQuantity(),
                entity.getStatus(),
                entity.getCreatedAt());
    }

    public Long getId() {
        return id;
    }

    public String getSkuId() {
        return skuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
