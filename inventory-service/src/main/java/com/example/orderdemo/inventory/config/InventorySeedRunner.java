package com.example.orderdemo.inventory.config;

import com.example.orderdemo.inventory.domain.InventoryItem;
import com.example.orderdemo.inventory.redis.AtomicStockDeductor;
import com.example.orderdemo.inventory.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Ensures demo SKU exists and Redis stock is warm after boot.
 *
 * @author jiaolin
 */
@Component
public class InventorySeedRunner implements CommandLineRunner {

    public static final String DEMO_SKU_ID = "SKU-DEMO-001";
    public static final String DEMO_SKU_NAME = "Upwork Portfolio Demo Widget";
    public static final int DEMO_STOCK = 100;

    private static final Logger LOGGER = LoggerFactory.getLogger(InventorySeedRunner.class);

    private final InventoryRepository inventoryRepository;
    private final AtomicStockDeductor atomicStockDeductor;

    public InventorySeedRunner(InventoryRepository inventoryRepository, AtomicStockDeductor atomicStockDeductor) {
        this.inventoryRepository = inventoryRepository;
        this.atomicStockDeductor = atomicStockDeductor;
    }

    /**
     * Seed MySQL row if missing and always warm Redis from MySQL.
     *
     * @param args CLI args (unused)
     */
    @Override
    @Transactional
    public void run(String... args) {
        InventoryItem item = inventoryRepository.findById(DEMO_SKU_ID)
                .orElseGet(() -> inventoryRepository.save(
                        new InventoryItem(DEMO_SKU_ID, DEMO_SKU_NAME, DEMO_STOCK)));
        atomicStockDeductor.warm(item.getSkuId(), item.getStock());
        LOGGER.info("Seeded/warmed inventory skuId={}, stock={}", item.getSkuId(), item.getStock());
    }
}
