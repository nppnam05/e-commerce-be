# E-commerce API

## 📝 Description
A robust and scalable RESTful API built for an E-commerce platform using Spring Boot. This backend project provides essential services including user authentication, security, and data persistence for an e-commerce system.

## 📂 Folder Structure

```text
e-commerce-api/
├── src/main/java/com/e_commerce/e_commerce_api/
│   ├── config/         # Application configurations (Security, Beans, etc.)
│   ├── constant/       # Global constants and enums
│   ├── controller/     # REST controllers handling HTTP requests
│   ├── dto/            # Data Transfer Objects (Request/Response models)
│   ├── entity/         # JPA Entities mapping to database tables
│   ├── exception/      # Custom exceptions and global error handling
│   ├── mapper/         # MapStruct interfaces for object mapping
│   ├── repository/     # Spring Data JPA repositories for database access
│   ├── service/        # Core business logic implementation
│   ├── utils/          # Utility and helper classes
│   └── ECommerceApiApplication.java # Application entry point
├── src/main/resources/ # Application properties and static resources
└── pom.xml             # Maven dependencies and build configuration
```

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 4.0.6**
  - Spring Web
  - Spring Security
  - Spring Data JPA
  - Spring Validation
- **Database:** PostgreSQL
- **Security:** JWT (JSON Web Tokens)
- **Documentation:** Swagger UI / OpenAPI (Springdoc)
- **Utilities:** Lombok, MapStruct
- **Build Tool:** Maven

## 🚀 Common Commands

Here are some of the most frequently used Maven commands for development:

### Clean and Compile
Removes the `target/` directory and recompiles the source code. This is useful for ensuring all generated sources (like MapStruct mappers) are up to date.
```bash
mvn clean compile
```

### Run the Application
Starts the Spring Boot application locally with the embedded Tomcat server.
```bash
mvn spring-boot:run
```

### Build and Package
Compiles the code, runs the tests, and packages the application into an executable JAR file inside the `target/` directory.
```bash
mvn clean package
```
