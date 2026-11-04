package com.example.orderdemo.inventory.repository;

import com.example.orderdemo.inventory.domain.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Persistence for inventory rows.
 *
 * @author jiaolin
 */
public interface InventoryRepository extends JpaRepository<InventoryItem, String> {

    /**
     * Conditionally deduct stock in MySQL to keep durability aligned with Redis.
     *
     * @param skuId SKU identifier
     * @param quantity positive quantity to deduct
     * @return number of rows updated (0 means insufficient stock or missing SKU)
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE InventoryItem i SET i.stock = i.stock - :quantity "
            + "WHERE i.skuId = :skuId AND i.stock >= :quantity")
    int deductIfEnough(@Param("skuId") String skuId, @Param("quantity") int quantity);
}
