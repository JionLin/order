package com.example.orderdemo.order.client.dto;

/**
 * Feign DTO for inventory details.
 *
 * @author jiaolin
 */
public class InventoryView {

    private String skuId;
    private String skuName;
    private Integer stock;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
