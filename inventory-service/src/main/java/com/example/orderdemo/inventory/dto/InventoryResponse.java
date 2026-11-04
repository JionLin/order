package com.example.orderdemo.inventory.dto;

/**
 * Inventory view returned to callers.
 *
 * @author jiaolin
 */
public class InventoryResponse {

    private final String skuId;
    private final String skuName;
    private final Integer stock;

    public InventoryResponse(String skuId, String skuName, Integer stock) {
        this.skuId = skuId;
        this.skuName = skuName;
        this.stock = stock;
    }

    public String getSkuId() {
        return skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public Integer getStock() {
        return stock;
    }
}
