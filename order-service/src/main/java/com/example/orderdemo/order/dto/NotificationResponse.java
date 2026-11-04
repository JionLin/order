package com.example.orderdemo.order.dto;

import com.example.orderdemo.order.domain.NotificationEntity;
import java.time.Instant;

/**
 * Notification API response.
 *
 * @author jiaolin
 */
public class NotificationResponse {

    private final Long id;
    private final Long orderId;
    private final String skuId;
    private final String message;
    private final Instant createdAt;

    public NotificationResponse(Long id, Long orderId, String skuId, String message, Instant createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.skuId = skuId;
        this.message = message;
        this.createdAt = createdAt;
    }

    /**
     * Map entity to response.
     *
     * @param entity notification entity
     * @return response DTO
     */
    public static NotificationResponse from(NotificationEntity entity) {
        return new NotificationResponse(
                entity.getId(),
                entity.getOrderId(),
                entity.getSkuId(),
                entity.getMessage(),
                entity.getCreatedAt());
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getSkuId() {
        return skuId;
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
