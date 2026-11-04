package com.example.orderdemo.inventory.dto;

/**
 * Result of a successful stock deduct.
 *
 * @author jiaolin
 */
public class DeductResponse {

    private final String skuId;
    private final Integer deducted;
    private final Integer remainingStock;

    public DeductResponse(String skuId, Integer deducted, Integer remainingStock) {
        this.skuId = skuId;
        this.deducted = deducted;
        this.remainingStock = remainingStock;
    }

    public String getSkuId() {
        return skuId;
    }

    public Integer getDeducted() {
        return deducted;
    }

    public Integer getRemainingStock() {
        return remainingStock;
    }
}
