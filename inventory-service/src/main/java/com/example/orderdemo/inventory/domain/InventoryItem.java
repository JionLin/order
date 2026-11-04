package com.example.orderdemo.inventory.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

/**
 * SKU inventory row; MySQL is the durable source of truth.
 *
 * @author jiaolin
 */
@Entity
@Table(name = "inventory")
public class InventoryItem {

    @Id
    @Column(name = "sku_id", length = 64, nullable = false)
    private String skuId;

    @Column(name = "sku_name", length = 128, nullable = false)
    private String skuName;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Version
    @Column(name = "version")
    private Long version;

    protected InventoryItem() {
    }

    public InventoryItem(String skuId, String skuName, Integer stock) {
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

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getVersion() {
        return version;
    }
}
