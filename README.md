A modern Banking API built with Spring Boot 3, Java 21, and Docker. 
This project serves as a backend foundation for managing bank accounts, transactions, and user security in a containerized environment.

Technical Stack:
Framework: Spring Boot 3.2.x
Language: Java 21
Database: H2 (In-Memory)
Security: Spring Security (WebMVC)
Documentation: SpringDoc OpenAPI (Swagger UI)
Containerization: Docker

Prerequisites:
Java Development Kit (JDK) 21 or higher
Maven 3.9.0 or higher
Docker Desktop installed and running

Getting Started
1. Clone the Repository
Clone the project to your local machine:

git clone https://github.com/YOUR_USERNAME/Lizyea.git
cd Lizyea

2. Build the Application
Use Maven to compile the source code and package it into an executable JAR file.
This step ensures that the target folder is updated with the latest changes:

mvn clean package -DskipTests

3. Build and Run with Docker
Build the Docker image using the provided Dockerfile and start the container:
docker build -t bank-app:latest .
docker run -d -p 8080:8080 --name bank-app bank-app:latest

Access Points and Monitoring
Once the application is running, the following endpoints are available:

Base API Endpoint: http://localhost:8080
API Documentation (Swagger UI): http://localhost:8080/swagger-ui/index.html
H2 Database Console: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
User: sa
Password: (leave)

Development Configurations
Security Management
To facilitate API testing during the initial development phase, Spring Security's auto-configuration 
is explicitly excluded in the main application class. 
This removes the default login requirement for API endpoints and the H2 console.

Database Persistence
The current configuration uses an in-memory database. 
Data will be cleared every time the application or Docker container is restarted. 
For persistent storage, the datasource configuration in application.
Properties must be updated to a persistent database like PostgreSQL.

Version Compatibility
The project is compiled using Java 21 (Class File Version 65.0). 
Running the application on a Java Runtime Environment (JRE) older than version 21 will result 
in an UnsupportedClassVersionError. 
Ensure both the local environment and the Docker base image match this version.

## Features
- Account creation and account management
- Transaction workflow logic
- RESTful API structure
- Spring Security configuration
- H2 database console for local testing
- Swagger/OpenAPI documentation
- Dockerized deployment

## API Endpoints
| Method | Endpoint | Description |
|---|---|---|
| GET | /api/accounts | Get all accounts |
| POST | /api/accounts | Create a new account |
| POST | /api/transactions | Create a transaction |
| GET | /swagger-ui/index.html | API documentation |

## Architecture
- Controller layer for REST endpoints
- Service layer for business logic
- Repository layer with Spring Data JPA
- Entity layer for account and transaction models
- Security configuration for protected resources

## What I Learned
- Building REST APIs with Spring Boot
- Managing dependencies with Maven
- Using Docker for consistent runtime environments
- Configuring Spring Security
- Designing a data layer with JPA/Hibernate
