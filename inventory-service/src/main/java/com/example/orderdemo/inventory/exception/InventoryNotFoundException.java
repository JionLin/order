package com.example.orderdemo.inventory.exception;

/**
 * Raised when a SKU does not exist.
 *
 * @author jiaolin
 */
public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(String skuId) {
        super("Inventory not found for skuId=" + skuId);
    }
}
