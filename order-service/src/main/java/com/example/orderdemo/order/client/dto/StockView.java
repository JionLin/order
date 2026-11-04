package com.example.orderdemo.order.client.dto;

/**
 * Feign DTO for stock-only view.
 *
 * @author jiaolin
 */
public class StockView {

    private String skuId;
    private Integer stock;
    private String source;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
