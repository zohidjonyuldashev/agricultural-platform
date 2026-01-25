# Agricultural Digital Platform

Digital Procurement Platform for Agriculture & Food Production built with Spring Boot 3 and Java 21.

## Features

- **Product Management** - CRUD operations for farm products (Scenario 1)
- **Order Management** - Order processing with status tracking (Scenario 2)
- **Price Tracking** - Market price updates with rollback capability (Scenario 3)
- **Performance Monitoring** - Prometheus metrics for high-traffic scenarios (Scenario 4)
- **Daily Reports** - Dashboard statistics and system health (Scenario 5)

## Technology Stack

- Java 21
- Spring Boot 3.5.10
- PostgreSQL 18
- Micrometer + Prometheus
- Docker & Kubernetes
- Helm Charts
- GitHub Actions
- ArgoCD

## Quick Start

### Prerequisites

- Java 21
- Docker Desktop
- Maven 3.9+

### Run with Docker Compose

```bash
docker-compose up -d
```

### Run Locally

```bash
# Start PostgreSQL
docker run -d --name postgres \
  -e POSTGRES_DB=agricultural_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 postgres:16-alpine

# Run application
./mvnw spring-boot:run
```

## API Endpoints

| Endpoint                    | Method           | Description                |
|-----------------------------|------------------|----------------------------|
| `/api/products`             | GET, POST        | List/Create products       |
| `/api/products/{id}`        | GET, PUT, DELETE | Get/Update/Delete product  |
| `/api/orders`               | GET, POST        | List/Create orders         |
| `/api/orders/{id}/status`   | PATCH            | Update order status        |
| `/api/prices`               | GET, POST        | List/Create prices         |
| `/api/prices/{id}/rollback` | POST             | Rollback price to previous |
| `/api/reports/daily`        | GET              | Generate daily report      |
| `/actuator/prometheus`      | GET              | Prometheus metrics         |
| `/actuator/health`          | GET              | Health check               |

## Custom Metrics

- `products_created_total` - Counter for created products
- `orders_placed_total` - Counter for placed orders
- `price_updates_total` - Counter for price updates
- `active_products_count` - Gauge for available products
- `pending_orders_count` - Gauge for pending orders
- `current_average_price` - Gauge for average price
- `order_processing_duration_seconds` - Histogram for order processing time
- `seasonal_traffic_requests_total` - Counter for traffic during peak seasons

## License

MIT License