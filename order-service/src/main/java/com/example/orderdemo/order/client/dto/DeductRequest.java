package com.example.orderdemo.order.client.dto;

/**
 * Feign request body for inventory deduct.
 *
 * @author jiaolin
 */
public class DeductRequest {

    private Integer quantity;

    public DeductRequest() {
    }

    public DeductRequest(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
