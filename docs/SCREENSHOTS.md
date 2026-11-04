# Screenshot checklist

Real captures from the running Compose stack in this environment (headless Chromium). Files live in `docs/screenshots/`.

| # | File | Contents |
|---|------|----------|
| 1 | [`docs/screenshots/01-compose-swagger-up.png`](screenshots/01-compose-swagger-up.png) | Order-service Swagger UI (`/swagger-ui/index.html`) with Orders + Notifications ops |
| 2 | [`docs/screenshots/02-place-order-success.png`](screenshots/02-place-order-success.png) | `POST /api/orders` **201** body (`id=1`, `SKU-DEMO-001`, qty `2`) |
| 3 | [`docs/screenshots/03-inventory-stock-after-order.png`](screenshots/03-inventory-stock-after-order.png) | `GET .../stock` showing **98** from **redis** after qty=2 |
| 4 | [`docs/screenshots/04-order-list.png`](screenshots/04-order-list.png) | `GET /api/orders` listing the created order |

## How to re-capture

```bash
docker compose up --build -d
# run README curl steps, then open Swagger / pretty-print JSON and screenshot
```

No short video is included by design.
