package com.example.orderdemo.inventory.service;

import com.example.orderdemo.inventory.domain.InventoryItem;
import com.example.orderdemo.inventory.dto.DeductResponse;
import com.example.orderdemo.inventory.dto.InventoryResponse;
import com.example.orderdemo.inventory.dto.StockResponse;
import com.example.orderdemo.inventory.exception.InsufficientStockException;
import com.example.orderdemo.inventory.exception.InventoryNotFoundException;
import com.example.orderdemo.inventory.redis.AtomicStockDeductor;
import com.example.orderdemo.inventory.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Inventory use-cases: query and Redis-first atomic deduct with MySQL durability.
 *
 * @author jiaolin
 */
@Service
public class InventoryAppService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryAppService.class);
    private static final long REDIS_INSUFFICIENT = -1L;
    private static final long REDIS_MISSING = -2L;

    private final InventoryRepository inventoryRepository;
    private final AtomicStockDeductor atomicStockDeductor;

    public InventoryAppService(InventoryRepository inventoryRepository, AtomicStockDeductor atomicStockDeductor) {
        this.inventoryRepository = inventoryRepository;
        this.atomicStockDeductor = atomicStockDeductor;
    }

    /**
     * Load SKU inventory details, preferring Redis for the stock number when warm.
     *
     * @param skuId SKU identifier
     * @return inventory view
     */
    @Transactional(readOnly = true)
    public InventoryResponse getInventory(String skuId) {
        InventoryItem item = requireItem(skuId);
        Integer cached = atomicStockDeductor.getCachedStock(skuId);
        int stock = cached != null ? cached : item.getStock();
        return new InventoryResponse(item.getSkuId(), item.getSkuName(), stock);
    }

    /**
     * Return stock only; warm Redis from MySQL on cache miss.
     *
     * @param skuId SKU identifier
     * @return stock view with source label
     */
    @Transactional(readOnly = true)
    public StockResponse getStock(String skuId) {
        Integer cached = atomicStockDeductor.getCachedStock(skuId);
        if (cached != null) {
            return new StockResponse(skuId, cached, "redis");
        }
        InventoryItem item = requireItem(skuId);
        atomicStockDeductor.warm(skuId, item.getStock());
        return new StockResponse(skuId, item.getStock(), "mysql");
    }

    /**
     * Deduct stock: Redis Lua atomic check first, then MySQL conditional update.
     *
     * @param skuId SKU identifier
     * @param quantity positive quantity
     * @return deduct result including remaining Redis stock
     */
    @Transactional
    public DeductResponse deduct(String skuId, int quantity) {
        requireItem(skuId);
        ensureRedisWarm(skuId);

        long redisRemaining = atomicStockDeductor.deduct(skuId, quantity);
        if (redisRemaining == REDIS_MISSING) {
            throw new InventoryNotFoundException(skuId);
        }
        if (redisRemaining == REDIS_INSUFFICIENT) {
            throw new InsufficientStockException(skuId, quantity);
        }

        int updatedRows = inventoryRepository.deductIfEnough(skuId, quantity);
        if (updatedRows == 0) {
            atomicStockDeductor.restore(skuId, quantity);
            throw new InsufficientStockException(skuId, quantity);
        }

        LOGGER.info("Deducted stock skuId={}, quantity={}, remainingRedis={}", skuId, quantity, redisRemaining);
        return new DeductResponse(skuId, quantity, (int) redisRemaining);
    }

    private InventoryItem requireItem(String skuId) {
        return inventoryRepository.findById(skuId)
                .orElseThrow(() -> new InventoryNotFoundException(skuId));
    }

    private void ensureRedisWarm(String skuId) {
        Integer cached = atomicStockDeductor.getCachedStock(skuId);
        if (cached != null) {
            return;
        }
        InventoryItem item = requireItem(skuId);
        atomicStockDeductor.warm(skuId, item.getStock());
        LOGGER.debug("Warmed Redis stock for skuId={} value={}", skuId, item.getStock());
    }
}
