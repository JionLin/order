package com.example.orderdemo.inventory.redis;

import java.util.Collections;
import java.util.List;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

/**
 * Atomically checks and deducts stock in Redis via Lua.
 * <p>
 * Pattern: Redis holds a hot stock counter; Lua DECRBY is all-or-nothing.
 * Caller persists the same deduct to MySQL after Redis success.
 * </p>
 *
 * @author jiaolin
 */
@Component
public class AtomicStockDeductor {

    private static final String DEDUCT_LUA =
            "local current = redis.call('GET', KEYS[1]) "
                    + "if current == false then return -2 end "
                    + "local stock = tonumber(current) "
                    + "local qty = tonumber(ARGV[1]) "
                    + "if stock < qty then return -1 end "
                    + "return redis.call('DECRBY', KEYS[1], qty)";

    private final StringRedisTemplate stringRedisTemplate;
    private final DefaultRedisScript<Long> deductScript;

    public AtomicStockDeductor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.deductScript = new DefaultRedisScript<>();
        this.deductScript.setScriptText(DEDUCT_LUA);
        this.deductScript.setResultType(Long.class);
    }

    /**
     * Atomically deduct quantity from Redis stock.
     *
     * @param skuId SKU identifier
     * @param quantity positive quantity
     * @return remaining stock after deduct; -1 insufficient; -2 missing key
     */
    public long deduct(String skuId, int quantity) {
        List<String> keys = Collections.singletonList(StockRedisKeys.stockKey(skuId));
        Long result = stringRedisTemplate.execute(deductScript, keys, String.valueOf(quantity));
        if (result == null) {
            return -2L;
        }
        return result;
    }

    /**
     * Roll back a previous Redis deduct when MySQL persistence fails.
     *
     * @param skuId SKU identifier
     * @param quantity quantity to restore
     */
    public void restore(String skuId, int quantity) {
        stringRedisTemplate.opsForValue().increment(StockRedisKeys.stockKey(skuId), quantity);
    }

    /**
     * Warm or overwrite Redis stock value from MySQL.
     *
     * @param skuId SKU identifier
     * @param stock stock amount
     */
    public void warm(String skuId, int stock) {
        stringRedisTemplate.opsForValue().set(StockRedisKeys.stockKey(skuId), String.valueOf(stock));
    }

    /**
     * Read cached stock; null when key is absent.
     *
     * @param skuId SKU identifier
     * @return cached stock or null
     */
    public Integer getCachedStock(String skuId) {
        String value = stringRedisTemplate.opsForValue().get(StockRedisKeys.stockKey(skuId));
        if (value == null) {
            return null;
        }
        return Integer.valueOf(value);
    }
}
