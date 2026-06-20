# E-Commerce API

A robust and scalable RESTful API built for an e-commerce platform using Spring Boot 3.4. This backend provides authentication, product management, order processing, and inventory tracking.

## 🎯 Features

- **Authentication & Security**
  - JWT-based authentication (Access + Refresh tokens)
  - OAuth2 social login (Google)
  - Role-based access control (RBAC)
  - Account lockout mechanism (5 failed attempts)

- **Core Functionality**
  - Product catalog with variants (colors, sizes)
  - Shopping cart management
  - Order processing with pessimistic locking (race condition prevention)
  - Online payment via PayOS (payment link generation & webhook handling)
  - User session management with device tracking

- **Storage & File Management**
  - MinIO integration for image uploads (S3-compatible)
  - Product image management

## 🛠️ Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 3.4
  - Spring Security 6
  - Spring Data JPA
  - Spring Validation
- **Database:** PostgreSQL with Flyway migrations & manual indexing strategy
- **File Storage:** MinIO (S3-compatible)
- **Payment:** PayOS (Vietnamese payment gateway)
- **Documentation:** Swagger UI / OpenAPI
- **Build Tool:** Maven
- **Utilities:** Lombok, MapStruct

## 📂 Project Structure

```
e-commerce-api/
└── src/main/java/com/e_commerce/e_commerce_api/
    ├── config/         # Spring Security & Application configs
    ├── controller/     # REST endpoints
    ├── service/        # Business logic
    ├── repository/     # Data access layer
    ├── entity/         # JPA entities
    ├── dto/            # Request/Response DTOs
    ├── mapper/         # MapStruct mappers
    ├── exception/      # Custom exceptions & global handler
    └── utils/          # Utility classes
```

## 🚀 Getting Started

### Prerequisites

- Java 21+
- PostgreSQL 12+
- Maven 3.8+
- MinIO (optional, for file uploads)

### Installation

1. **Clone repository**

```bash
git clone https://github.com/nppnam05/e-commerce-be.git
cd e-commerce-be
```

2. **Configure environment variables**

```bash
cp .env.example .env
# Edit .env with your database credentials, JWT secrets, MinIO config
```

3. **Build & Run**

```bash
mvn clean install
mvn spring-boot:run
```

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI:** `http://localhost:8080/api/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/api/v3/api-docs`

### Key Endpoints

**Authentication:**

```
POST /api/auth/login       - User login
POST /api/auth/sign-up     - Register new account
POST /api/auth/refresh     - Refresh access token
POST /api/auth/logout      - Logout
```

**Products:**

```
GET    /api/products        - List products (paginated)
GET    /api/products/{id}   - Get product details
POST   /api/products        - Create product (Admin only)
PUT    /api/products/{id}   - Update product (Admin only)
DELETE /api/products/{id}   - Delete product (Admin only)
```

**Orders:**

```
GET  /api/orders           - Get user orders
GET  /api/orders/{id}      - Order details
POST /api/orders           - Create order
```

**Payments (PayOS):**

```
POST /api/payments/create      - Create PayOS payment link
POST /api/payments/webhook     - Receive PayOS payment webhook
GET  /api/payments/cancel/{id} - Cancel payment
```

> See full API documentation in Swagger UI.

## 🔐 Security Features

- **JWT Authentication** — Stateless, scalable authentication
- **Token Rotation** — Automatic refresh token rotation
- **HttpOnly Cookies** — Protected against XSS attacks
- **CORS Configuration** — Configurable allowed origins
- **OAuth2 Integration** — Google social login
- **Account Protection** — Failed login attempt tracking & lockout (5 attempts)
- **Device Tracking** — Session management by device ID and IP

## ⚡ Concurrency & Payment

### Pessimistic Locking (OrderService)

When creating an order, the system uses `@Lock(LockModeType.PESSIMISTIC_WRITE)` on the Stock query to prevent race conditions — ensuring no two concurrent requests can deduct stock for the same product at the same time:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT s FROM Stock s WHERE s.productChildren.id = :id")
Optional<Stock> findByProductChildrenIdWithLock(@Param("id") Integer id);
```

The transaction holds a row-level lock until commit/rollback — subsequent requests must wait, preventing overselling.

### PayOS Integration (PaymentService)

Integrated with [PayOS](https://payos.vn) — a Vietnamese payment gateway — to support online payments:

- **Create payment link** — Calls the PayOS API to generate a payment URL linked to the `orderId`
- **Webhook confirmation** — Receives a callback from PayOS after successful payment and updates the order status

## 📊 Database Schema

The database is organized into 3 schemas:

| Schema        | Description                                         |
| ------------- | --------------------------------------------------- |
| `identity`    | Users, roles, authentication sessions               |
| `inventories` | Products, categories, colors, sizes, images, stocks |
| `sales`       | Shopping carts, orders, order items                 |

Migrations are managed with **Flyway** for version control and reproducibility.

## 🗂️ Database Indexing Strategy

Indexes were added based on analysis of actual query patterns (via `EXPLAIN ANALYZE` and code review), not applied indiscriminately.

### Foreign Key Indexes (JOIN optimization)

PostgreSQL only automatically indexes Primary Keys, not Foreign Keys — so FK columns frequently used in JOINs were manually indexed:

| Table              | Column                           | Purpose                                             |
| ------------------ | -------------------------------- | --------------------------------------------------- |
| `carts`            | `UserId`, `ProductChildrenId`    | JOIN when fetching cart with product details        |
| `orders`           | `UserId`                         | JOIN when fetching orders by user                   |
| `order_products`   | `OrderId`, `ProductChildrenId`   | JOIN when building order line items                 |
| `product_children` | `ProductId`, `ColorId`, `SizeId` | JOIN when fetching product variants                 |
| `product_images`   | `ProductId`                      | JOIN when aggregating product images (`STRING_AGG`) |
| `stocks`           | `ProductChildrenId`              | JOIN + lock when checking/deducting stock           |
| `favorites`        | `UserId`, `ProductChildrenId`    | JOIN when fetching favorite products                |
| `addresses`        | `UserId`                         | JOIN when fetching shipping addresses               |
| `users`            | `RoleId`                         | JOIN when fetching user roles                       |

### Authentication Hot-Path Index

`user_session` is queried on **every JWT-authenticated request** (via `JwtAuthenticationFilter`) — making it the most frequently queried table in the system:

| Column         | Purpose                                                 |
| -------------- | ------------------------------------------------------- |
| `SessionToken` | Verifies the token on every request                     |
| `DeviceId`     | Finds the session by device during login/refresh/logout |

### Composite Index for Filtering + Sorting

```sql
CREATE INDEX idx_products_status_created
    ON products("Status", "CreatedOn" DESC);
```

Applied to queries following a "filter by status + sort by time" pattern (product listings, order dashboard) — avoids PostgreSQL having to re-sort after filtering.

## 🐳 Docker & Deployment

**Build Docker image:**

```bash
docker build -t e-commerce-api:latest .
```

**Run with Docker Compose:**

```bash
docker-compose up -d
```

**CI/CD Pipeline** — The project uses Jenkins for automated deployment:

- Triggered by GitHub push events via webhook
- Automated build, test, and deployment to VPS

**Live Demo:** http://nam23211.id.vn:3000

## 🔗 Related Projects

- [Frontend](https://github.com/nppnam05/e-commerce-fe)
- [Database Schema](https://github.com/nppnam05/e-commerce-database)

## 👨‍💻 Author

**Nguyen Pham Phuong Nam**

- GitHub: [@nppnam05](https://github.com/nppnam05)
- Email: nppnam05@gmail.com
