package com.example.orderdemo.order.exception;

/**
 * Raised when an order id does not exist.
 *
 * @author jiaolin
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long orderId) {
        super("Order not found for id=" + orderId);
    }
}
