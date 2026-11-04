package com.example.orderdemo.inventory.exception;

/**
 * Raised when Redis/MySQL stock cannot cover the requested quantity.
 *
 * @author jiaolin
 */
public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String skuId, int quantity) {
        super("Insufficient stock for skuId=" + skuId + ", requested=" + quantity);
    }
}
