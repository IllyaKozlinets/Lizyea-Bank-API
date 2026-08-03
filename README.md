# Lizyea Bank API

A modern Banking API built with Spring Boot, Java 21, and Docker. This project serves as a backend foundation for managing bank accounts, transactions, and user security in a containerized environment.

## Technical Stack

- **Framework:** Spring Boot 3.2.x
- **Language:** Java 21
- **Database:** MySQL (with Flyway for schema migrations; H2 in-memory used for local testing)
- **Security:** Spring Security (WebMVC)
- **Documentation:** SpringDoc OpenAPI (Swagger UI)
- **Testing:** JUnit 5, Spring Boot Test, Spring Security Test
- **Containerization:** Docker, Docker Compose

## Prerequisites

- Java Development Kit (JDK) 21 or higher
- Maven 3.9.0 or higher
- Docker Desktop installed and running
- MySQL 8+ (or use the bundled Docker Compose service)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/IllyaKozlinets/Lizyea-Bank-API.git
cd Lizyea-Bank-API
```

### 2. Configure the Database

Update `application.properties` (or your environment variables) with your MySQL connection details:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lizyea_bank
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Flyway will automatically run migrations on startup to set up the schema.

### 3. Build the Application

```bash
mvn clean package
```

This compiles the source, runs the test suite, and packages the application into an executable JAR.

To skip tests during the build:

```bash
mvn clean package -DskipTests
```

### 4. Build and Run with Docker

```bash
docker build -t bank-app:latest .
docker run -d -p 8080:8080 --name bank-app bank-app:latest
```

Or, to run the full stack (app + MySQL) with Docker Compose:

```bash
docker-compose up -d
```

## Access Points

Once the application is running, the following endpoints are available:

- **Base API Endpoint:** http://localhost:8080
- **API Documentation (Swagger UI):** http://localhost:8080/swagger-ui/index.html

## Running Tests

The project includes unit and integration tests covering the transactional service layer:

```bash
mvn test
```

## Development Notes

**Security Configuration**
To facilitate API testing during development, Spring Security's auto-configuration is explicitly excluded in the main application class, removing the default login requirement for API endpoints.

**Database Persistence**
Production data is persisted in MySQL via Flyway-managed migrations. H2 remains available as an in-memory database for fast local test runs and is not used in the running application.

**Version Compatibility**
The project is compiled using Java 21 (Class File Version 65.0). Running the application on a JRE older than version 21 will result in an `UnsupportedClassVersionError`. Ensure both the local environment and the Docker base image match this version.

## Features

- Account creation and account management
- Transaction workflow logic
- RESTful API structure
- Spring Security configuration
- MySQL persistence with Flyway migrations
- Unit and integration test coverage for the transactional service
- Swagger/OpenAPI documentation
- Dockerized deployment (app + database via Docker Compose)

## API Endpoints

| Method | Endpoint               | Description           |
|--------|------------------------|------------------------|
| GET    | /api/accounts          | Get all accounts      |
| POST   | /api/accounts          | Create a new account  |
| POST   | /api/transactions      | Create a transaction  |
| GET    | /swagger-ui/index.html | API documentation     |

## Architecture

- Controller layer for REST endpoints
- Service layer for business logic
- Repository layer with Spring Data JPA
- Entity layer for account and transaction models
- Security configuration for protected resources

## What I Learned

- Building REST APIs with Spring Boot
- Managing dependencies with Maven
- Designing a data layer with JPA/Hibernate and migrating from an in-memory database to MySQL with Flyway
- Writing unit and integration tests for service-layer business logic
- Using Docker and Docker Compose for consistent, multi-container runtime environments
- Configuring Spring Security
