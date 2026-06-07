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
  - Order processing and tracking
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
- **Database:** PostgreSQL with Flyway migrations
- **File Storage:** MinIO (S3-compatible)
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
GET  /api/orders       - Get user orders
GET  /api/orders/{id}  - Order details
POST /api/orders       - Create order
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

## 📊 Database Schema

The database is organized into 3 schemas:

| Schema | Description |
|--------|-------------|
| `identity` | Users, roles, authentication sessions |
| `inventories` | Products, categories, colors, sizes, images, stocks |
| `sales` | Shopping carts, orders, order items |

Migrations are managed with **Flyway** for version control and reproducibility.

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

## 👨‍💻 Author

**Nguyen Pham Phuong Nam**
- GitHub: [@nppnam05](https://github.com/nppnam05)
- Email: nppnam05@gmail.com