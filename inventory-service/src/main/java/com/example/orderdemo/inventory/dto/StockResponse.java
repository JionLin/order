package com.example.orderdemo.inventory.dto;

/**
 * Lightweight stock-only response.
 *
 * @author jiaolin
 */
public class StockResponse {

    private final String skuId;
    private final Integer stock;
    private final String source;

    public StockResponse(String skuId, Integer stock, String source) {
        this.skuId = skuId;
        this.stock = stock;
        this.source = source;
    }

    public String getSkuId() {
        return skuId;
    }

    public Integer getStock() {
        return stock;
    }

    public String getSource() {
        return source;
    }
}
