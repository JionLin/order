package com.example.orderdemo.order.event;

import org.springframework.context.ApplicationEvent;

/**
 * Published after an order is created and inventory deducted.
 *
 * @author jiaolin
 */
public class OrderPlacedEvent extends ApplicationEvent {

    private final Long orderId;
    private final String skuId;
    private final Integer quantity;

    public OrderPlacedEvent(Object source, Long orderId, String skuId, Integer quantity) {
        super(source);
        this.orderId = orderId;
        this.skuId = skuId;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getSkuId() {
        return skuId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
