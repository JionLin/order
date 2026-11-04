package com.example.orderdemo.order.exception;

/**
 * Propagated when inventory-service rejects deduct for insufficient stock.
 *
 * @author jiaolin
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String detail) {
        super(detail == null || detail.isBlank() ? "Insufficient stock" : detail);
    }
}
