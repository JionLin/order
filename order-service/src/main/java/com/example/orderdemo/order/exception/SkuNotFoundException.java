package com.example.orderdemo.order.exception;

/**
 * Propagated when inventory-service cannot find a SKU.
 *
 * @author jiaolin
 */
public class SkuNotFoundException extends RuntimeException {

    public SkuNotFoundException(String detail) {
        super(detail == null || detail.isBlank() ? "SKU not found" : detail);
    }
}
