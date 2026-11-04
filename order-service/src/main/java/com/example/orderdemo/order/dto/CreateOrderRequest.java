package com.example.orderdemo.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Create-order request body.
 *
 * @author jiaolin
 */
public class CreateOrderRequest {

    @NotBlank
    private String skuId;

    @NotNull
    @Min(1)
    private Integer quantity;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
