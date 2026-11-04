package com.example.orderdemo.inventory.redis;

/**
 * Redis key helpers for stock cache.
 *
 * @author jiaolin
 */
public final class StockRedisKeys {

    private static final String STOCK_PREFIX = "inventory:stock:";

    private StockRedisKeys() {
    }

    /**
     * Build Redis key for a SKU stock counter.
     *
     * @param skuId SKU identifier
     * @return Redis key
     */
    public static String stockKey(String skuId) {
        return STOCK_PREFIX + skuId;
    }
}
