# Order Demo (Upwork Portfolio)

Self-built **Spring Boot 3** mini order-domain demo: place order → deduct inventory → in-process notification.

**Not** a production seckill / flash-sale system. No Kafka/RabbitMQ cluster, no OAuth, no payment.

**Tech highlight:** OpenFeign (order → inventory) + Redis Lua atomic stock deduct, with MySQL as durable store.

## Architecture

| Service | Port | Role |
|---------|------|------|
| `order-service` | 8081 | Create/query orders; OpenFeign to inventory; `ApplicationEvent` → async notification listener |
| `inventory-service` | 8082 | Stock query + deduct; Redis hot counter + MySQL source of truth |
| MySQL 8.4 | 3306 | Orders, notifications, inventory rows |
| Redis 7.4 | 6379 | Atomic stock check/deduct (Lua) |

### Redis pattern (chosen)

1. MySQL holds durable stock (`inventory` table).
2. On boot, `CommandLineRunner` warms Redis key `inventory:stock:{skuId}`.
3. Deduct runs a **Redis Lua** script (GET + compare + DECRBY) so concurrent oversell fails atomically.
4. After Redis success, MySQL runs `UPDATE ... WHERE stock >= qty`. If MySQL rejects, Redis stock is restored.

Seed SKU: `SKU-DEMO-001` with stock **100**.

## Pinned versions

| Component | Version |
|-----------|---------|
| Java | 17 (Temurin runtime in images) |
| Spring Boot | **3.3.5** |
| Spring Cloud (OpenFeign BOM only) | **2023.0.3** |
| springdoc-openapi | **2.6.0** |
| MySQL image | **8.4.3** |
| Redis image | **7.4.1-alpine** |
| Maven | 3.9.x (Docker build) / 3.8+ local |

No Config Server, Eureka, Bus, or API Gateway.

## Quick start

```bash
cp .env.example .env
# Optional on some constrained Linux VMs if container↔container TCP stalls:
#   sudo bash scripts/fix-docker-bridge.sh
docker compose up --build -d
docker compose ps
```

Wait until both app healthchecks are healthy, then open:

- Order Swagger UI: http://localhost:8081/swagger-ui.html
- Inventory Swagger UI: http://localhost:8082/swagger-ui.html
- Health: http://localhost:8081/actuator/health · http://localhost:8082/actuator/health

Stop:

```bash
docker compose down
```

## Acceptance checklist (curl)

Replace nothing if you use default ports from `.env.example`.

### 1) Seed stock visible

```bash
curl -s http://localhost:8082/api/inventory/SKU-DEMO-001 | jq
curl -s http://localhost:8082/api/inventory/SKU-DEMO-001/stock | jq
```

Expect `"stock": 100` (or current remaining after prior orders).

### 2) Place order → 2xx + order id; stock drops; notification appears

```bash
curl -s -X POST http://localhost:8081/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"skuId":"SKU-DEMO-001","quantity":2}' | jq

curl -s http://localhost:8082/api/inventory/SKU-DEMO-001/stock | jq
# stock should be previous - 2

curl -s http://localhost:8081/api/orders | jq
sleep 1
curl -s http://localhost:8081/api/notifications | jq
```

Expect notification message like `Order N placed for sku SKU-DEMO-001 x2` (also logged as `Notification stored: ...` in `order-service`).

### 3) Oversell / insufficient stock → **409 Conflict**

```bash
curl -s -o /tmp/oversell.json -w "%{http_code}\n" -X POST http://localhost:8081/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"skuId":"SKU-DEMO-001","quantity":999999}'
cat /tmp/oversell.json | jq
```

Expected HTTP status: **409**. Body explains insufficient stock. No new successful order for that request.

### 4) Actuator

```bash
curl -s http://localhost:8081/actuator/health | jq
curl -s http://localhost:8082/actuator/health | jq
```

## REST surface

| Method | Path | Service |
|--------|------|---------|
| POST | `/api/orders` | order |
| GET | `/api/orders/{id}` | order |
| GET | `/api/orders` | order |
| GET | `/api/notifications` | order |
| GET | `/api/inventory/{skuId}` | inventory |
| GET | `/api/inventory/{skuId}/stock` | inventory |
| POST | `/api/inventory/{skuId}/deduct` | inventory (Feign + demo) |
| GET | `/actuator/health` | both |

## Local Maven build (optional)

```bash
mvn -q clean package -DskipTests
```

## Screenshots

See [docs/SCREENSHOTS.md](docs/SCREENSHOTS.md) and PNGs under `docs/screenshots/`.

## Honest portfolio wording

This repository is a **readable teaching/demo stack** for interviews and Upwork profiles: two deployables, visible OpenFeign client, Redis Lua deduct, MySQL persistence, and an in-process `@TransactionalEventListener` notification path. It is intentionally small—not a marketplace, not MoR/payment, and not a claimed high-QPS seckill platform.
