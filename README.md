# POS Microservices

A Point of Sale (POS) system built using a microservices architecture with modern Java and Spring Boot. This project leverages a centralized Dockerfile configuration and Docker Compose for seamless development, testing, and deployment.

## 🚀 Architecture Overview

This project consists of several microservices coordinated via an API Gateway and Service Registry.

### Core Services
- **ServiceDiscovery** (`:8761`): Netflix Eureka server providing service registration and discovery.
- **ApiGateway** (`:9010`): Spring Cloud Gateway acting as the central entry point for all client requests.
- **AuthService** (`:9013`): Handles JWT-based authentication, authorization, and secure access.
- **UserService** (`:9012`): Manages user accounts, roles, and profiles.
- **StoreService** (`:9014`): Handles operations related to physical or virtual store locations.
- **ProductService** (`:9015`): Manages the product catalog, inventory, and categories.
- **UtilityResource**: A shared Maven module containing common libraries, DTOs, exceptions, and utility functions used by other microservices.

### Infrastructure Services
- **MySQL** (`:3307` externally): Primary relational database for microservices (`pos_user_service`, `pos_auth_service`, `pos_store_service`, `pos_product_service`).
- **PostgreSQL** (`:5432`): Secondary relational database with pgAdmin (`:5050`) for administration.
- **Redis Stack** (`:6379` & `:8001` UI): In-memory data store for caching and session state.

## 🛠️ Technology Stack

- **Platform**: Java 25
- **Framework**: Spring Boot 4.0.0
- **Cloud**: Spring Cloud 2025.1.0
- **Build Tool**: Maven
- **Containerization**: Docker, Docker Compose (with Hot-Reloading via `develop/watch`)
- **Other**: Lombok, Spring Boot DevTools

## ⚙️ Prerequisites

- **Docker** and **Docker Compose** installed on your machine.
- **Java 25** and **Maven** (if you wish to build or run locally without Docker).

## 🏃 Getting Started

### 1. Environment Setup
Copy the example environment variables file and configure your credentials:
```bash
cp .env.example .env
```
Ensure you fill in necessary values like `JWT_SECRET`, database passwords, etc.

### 2. Running with Docker Compose
The project features a single centralized `Dockerfile` using multi-stage builds. Docker Compose is set up to build and start all microservices and infrastructure.

```bash
docker-compose up --build
```

### 3. Hot-Reloading in Development
The `docker-compose.yml` is configured with Docker's `develop` watch mode. When running `docker-compose up --watch`, changes made to the `src` directories or `pom.xml` files locally will automatically sync and trigger a restart via Spring Boot DevTools. 

To use hot-reloading:
```bash
docker-compose watch
```

## 📂 Project Structure

```text
pos-microservices/
├── ApiGateway/          # API Gateway service
├── AuthService/         # Authentication and authorization
├── ProductService/      # Product management
├── ServiceDiscovery/    # Eureka registry
├── StoreService/        # Store management
├── UserService/         # User management
├── UtilityResource/     # Shared dependencies and common code
├── docker-compose.yml   # Multi-service container orchestration
├── Dockerfile           # Unified multi-stage build script
└── pom.xml              # Maven parent pom for module definitions
```

## 🧑‍💻 Debugging
Remote debugging is enabled for the microservices. You can attach your IDE debugger to the respective ports:
- UserService: `5006`
- AuthService: `5005`
- StoreService: `5007`
- ProductService: `5008`
