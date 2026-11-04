package com.example.orderdemo.order.client.dto;

/**
 * Feign response for inventory deduct.
 *
 * @author jiaolin
 */
public class DeductResponse {

    private String skuId;
    private Integer deducted;
    private Integer remainingStock;

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public Integer getDeducted() {
        return deducted;
    }

    public void setDeducted(Integer deducted) {
        this.deducted = deducted;
    }

    public Integer getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }
}
