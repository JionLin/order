-- Shared schema bootstrap for order-demo (demo credentials only).
CREATE DATABASE IF NOT EXISTS orderdemo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE orderdemo;

CREATE TABLE IF NOT EXISTS inventory (
    sku_id   VARCHAR(64)  NOT NULL PRIMARY KEY,
    sku_name VARCHAR(128) NOT NULL,
    stock    INT          NOT NULL,
    version  BIGINT       NULL
);

INSERT INTO inventory (sku_id, sku_name, stock, version)
VALUES ('SKU-DEMO-001', 'Upwork Portfolio Demo Widget', 100, 0)
ON DUPLICATE KEY UPDATE sku_name = VALUES(sku_name);
