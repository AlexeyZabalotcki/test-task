# Test Task - Pioneer Pixel

## Overview

This application is a RESTful service for managing users, their accounts, emails, and phones. It includes authentication, dynamic search, caching, scheduling, and transfer functionality.

## Features

- 3-layer architecture: Controller, Service, DAO (Repository)
- PostgreSQL persistence with JPA
- JWT-based authentication (email or phone + password)
- CRUD operations on user's emails and phones
- User search with filters (dateOfBirth, phone, email, name) and pagination
- Scheduled balance updates: +10% every 30 seconds (max 207% of initial deposit)
- Money transfer between users with transactional safety and pessimistic locking
- Caching of user data with simple in-memory cache
- Swagger UI for API documentation
- Logging with SLF4J
- Unit tests and integration tests using Testcontainers and MockMvc

## Technologies

- Java 17
- Spring Boot 3.4.5
- Spring Data JPA
- Spring Security
- Spring Cache
- Spring Scheduling
- jjwt 0.11.5 (JWT tokens)
- Lombok
- springdoc-openapi (Swagger UI)
- Testcontainers 1.18.3
- JUnit 5 & Mockito

## Prerequisites

- Java 17+
- Maven 3.6+
- PostgreSQL 12+ (or Docker)
- Docker (for running integration tests)

## Configuration

Configuration is done via `src/main/resources/application.yml`. The default settings can be overridden using environment variables:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/testtask
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
SPRING_CACHE_TYPE=redis             # or simple
SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379
JWT_SECRET=secret-key
JWT_EXPIRATION_MS=3600000
```

Example `application.yml`:

```yaml
spring:
  application:
    name: test-task

  datasource:
    url: jdbc:postgresql://localhost:5432/testtask
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver

  sql:
    init:
      platform: postgres
      mode: always
      continue-on-error: true

  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
    database: postgresql

  cache:
    type: redis

  data:
    redis:
      host: redis
      port: 6379

server:
  port: 8080
```

## Setup & Run

1. **Run PostgreSQL**

   Locally or via Docker:

   ```bash
   docker run -d --name testtask-db \
     -e POSTGRES_DB=testtask \
     -e POSTGRES_USER=postgres \
     -e POSTGRES_PASSWORD=postgres \
     -p 5432:5432 postgres:15
   ```

2. **Configure**

   The application uses environment variables or defaults:

   ```bash
   SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/testtask
   SPRING_DATASOURCE_USERNAME=postgres
   SPRING_DATASOURCE_PASSWORD=postgres

   JWT_SECRET=secret-key
   JWT_EXPIRATION_MS=3600000
   ```

3. **Build & Run (Local)**

   ```bash
   mvn clean package
   java -jar target/test-task-0.0.1-SNAPSHOT.jar
   ```

   The service starts on port `8080`.

### Docker & Docker Compose

To run the full stack (PostgreSQL, Redis, and the application) via Docker:

```bash
docker-compose up -d --build
```

Ensure Docker and Docker Compose are installed. After startup, the application is accessible at http://localhost:8080

4. **API Docs**

   Access Swagger UI at [http://localhost:8080/swagger-ui/index.html#](http://localhost:8080/swagger-ui/index.html#)

## Running Tests

- **Unit tests** (Transfer logic):

  ```bash
  mvn test -Dtest=TransferServiceImplTest
  ```

- **Integration tests** (AuthController): requires Docker:
  ```bash
  mvn test
  ```

## Design Decisions

- **Caching**: Applied on service layer for `getCurrentUser` and evicted on data changes.
- **Dynamic Search**: Used JPA Specifications for flexible filtering by multiple fields.
- **Scheduling**: Spring Scheduling to manage periodic balance updates.
- **Transfer**: Pessimistic locking (`PESSIMISTIC_WRITE`) for account rows to prevent race conditions. Transactional integrity ensured.
- **Security**: Simple JWT mechanism with minimal claims (userId), stateless sessions.
- **Testing**: Testcontainers to simulate real PostgreSQL; MockMvc for controller integration tests.

