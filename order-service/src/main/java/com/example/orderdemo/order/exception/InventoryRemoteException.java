package com.example.orderdemo.order.exception;

/**
 * Unexpected inventory remote failure.
 *
 * @author jiaolin
 */
public class InventoryRemoteException extends RuntimeException {

    public InventoryRemoteException(String message) {
        super(message);
    }
}
