# POS Microservices

A Point of Sale (POS) system built using a microservices architecture with modern Java and Spring Boot. This project leverages a centralized Dockerfile configuration and Docker Compose for seamless development, testing, and deployment.

## 🚀 Architecture Overview

```mermaid
graph TD
    Client[Client / Browser] -->|Port 9010| Gateway[ApiGateway]
    Gateway -->|Discovery| Discovery[ServiceDiscovery :8761]
    Gateway -->|Auth Guard| Auth[AuthService :9013]
    Gateway -->|Route & Propagate User Details| User[UserService :9012]
    Gateway -->|Route| Store[StoreService :9014]
    Gateway -->|Route| Product[ProductService :9015]

    Auth -->|Blacklist Tokens| Redis[(Redis Stack :6379)]
    User -->|DB| MySQL[(MySQL :3307)]
    Store -->|DB| MySQL
    Product -->|DB| MySQL
    Product -->|DB| Postgres[(PostgreSQL :5432)]
```

### Services & Infrastructure
- **Core:** 
- ApiGateway (`:9010`)
- ServiceDiscovery (`:8761`)
- AuthService (`:9013`)
- UserService (`:9012`) 
- StoreService (`:9014`) 
- ProductService (`:9015`) 
- UtilityResource (Shared Lib)


- **Databases:** 
- MySQL (`:3307`)
- PostgreSQL (`:5432`)
- Redis Stack (`:6379` / UI `:8001`)

## ✨ Key Features

### 🔐 Auth & User (`[3]`)
- **JWT Auth & Guard:** Stateless authentication & request filtering.
- **Propagation:** Token extraction & downstream forwarding of user details from API Gateway.
- **RBAC:** Role-based access control & permission mapping.
- **Token Blacklisting:** Redis-backed logout / invalidation handler.
- **Access Flows:** Password reset, email/phone verification, stateless cookie removal.

### 📦 Product Service
#### `[1]` File Handling
- **Uploads:** Multi-file upload.
- **Optimizer:** Image resize & compression.
- **Validation:** Upload validation (type, size).

#### `[2]` Data Operations
- **Pagination:** High-performance Keyset Pagination.
- **Search & Filter:** Basic & advanced queries by date, status, category.
- **Batching:** Sorting, bulk update/delete.
- **Lifecycle:** Soft delete, restore, and audit trail.

### 🌐 Global / DevOps (`[4]`)
- **Single Dockerfile:** Optimized multi-stage build script for all microservices.
- **Compose Anchors:** Docker Compose with YAML anchor reuse (`<<: *reuse_variable`).

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

## 🗄️ Database
### Java-Based Migrations
The project uses a custom `Schema` helper within standard Flyway Java migrations to simplify DDL operations.

**Example Migration (`S4__CreateProductImagesTable.java`):**
```java
@Component
public class S4__CreateProductImagesTable extends BaseMigration {
    @Override
    public void up(Schema schema) throws SQLException {
        schema.create("pos_product_images", table -> {
            table.uuid();
            table.foreignUuid("product_id").referencesTable("pos_products").onDeleteCascade();
            table.string("image_url", 500);
            table.bool("is_primary").defaultValue(false);
            table.integer("sort_order");
        });

    }

    @Override
    public void down(Schema schema) throws SQLException {
        schema.dropIfExists("pos_product_images");
    }
}
```

### 🌱 Data Seeding
Data seeding is handled via a flexible Factory and Seeder pattern, making it easy to generate mock data.
### Factories
Factories define the blueprint for your entities using `net.datafaker`.

**Example Factory (`UserFactory.java`):**
```java
@Component
@RequiredArgsConstructor
public class UserFactory extends Factory<User> {
    private final PasswordEncoder passwordEncoder;

    @Override
    public User definition() {
        return User.builder()
            .name(faker.name().name())
            .email(faker.internet().emailAddress())
            .password(passwordEncoder.encode("SuperSecretPass"))
            .build();
    }
}
```

### Seeders
Seeders orchestrate the data creation using Factories.

**Example Seeder Registration (`DatabaseSeeder.java`):**
```java
public void seed() {
    this.call(
            RolesSeeder.class,
            UserSeeder.class,
            ApartmentTypeSeeder.class
            // ... other seeders
    );
}
```
**Endpoint**: `GET /api/users/cursor?cursor={last_id}&pageSize={size}`


## ⚡️ Advanced Features

### Cursor Pagination
The application implements efficient cursor-based pagination for large datasets, as demonstrated in the User service.

**Logic Pattern (`UserServiceImpl.java`):**
```java
public CursorPageResponse<User> cursorPaginationPattern(Long cursor, int pageSize) {
    Pageable pageable = PageRequest.of(0, pageSize);
    List<User> users = userRepository.cursorPaginationPattern(cursor, pageable);
    boolean hasNext = users.size() == pageSize;

    Long nextCursor = hasNext
            ? users.getLast().getId()
            : null;
    return new CursorPageResponse<>(users, pageSize, nextCursor, hasNext);
}
```


### Custom Annotation for File Validation and Checking ID exists or not
Usage example

```java
import org.booking.Validations.File;
import org.springframework.web.multipart.MultipartFile;

public record StoreUserRequest(
        String name,

@File(mimeTypes = { MimeTypes.JPEG, MimeTypes.PNG, MimeTypes.WEBP, MimeTypes.GIF }, 
extensions = { FileExtension.JPG, FileExtension.JPEG, FileExtension.PNG, FileExtension.WEBP,FileExtension.GIF }, maxSize = 10)
        MultipartFile file,
        
        @Positive
        @NotNull(message = "cities ID is required for property!")
        @ExistsId(message = "Not found any cities from provided ID")
        Long cityId
) {}



```

```java
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.booking.cities.CityRepository;

@RequiredArgsConstructor
public class ExistsIdValidator implements ConstraintValidator<ExistsId, Long> {

    private final CityRepository cityRepository;

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {
        if (id == null) return true;
        return cityRepository.existsById(id);
    }
}
```

# 🚀 Dynamic JPA Specifications with `SpecificationBuilder`

Building complex, dynamic database queries in Spring Data JPA can often lead to messy, deeply nested `if` statements. The `SpecificationBuilder` is a utility class designed to solve this by providing a clean, fluent, and functional approach to crafting JPA `Specification`s.

This allows you to dynamically compose queries based on user input, optional parameters, and complex conditions while keeping your codebase highly readable and maintainable.

---

## 🌟 Key Features

* **Fluent API**: Chain conditions seamlessly, making the code read like natural language.
* **Functional Approach**: Pass method references (e.g., `spec::withCity`) directly to the builder.
* **Null-Safety Built-in**: The `.when()` methods automatically check for `null` parameters, preventing `NullPointerException`s and avoiding empty query conditions.
* **Eager Fetching Support**: Built-in support for resolving N+1 query problems by defining `LEFT JOIN` fetches using `.load()`.

---

## 🛠️ The `SpecificationBuilder` API

Here are the primary methods available in the `SpecificationBuilder`:

| Method | Description |
| :--- | :--- |
| `when(boolean condition, Specification<T> spec)` | Appends the specification only if the `condition` is `true`. |
| `when(V value, Function<V, Specification<T>> func)` | Appends the specification function result if the `value` is not `null`. |
| `whereId(V value, Function<V, Specification<T>> func)` | Functions similarly to `when()` but semantically emphasizes an ID-based lookup. |
| `load(String entity)` | Triggers a `LEFT JOIN` fetch on the specified entity string to avoid N+1 issues. |
| `load(Class<?> entityClz)` | Triggers a `LEFT JOIN` fetch on the specific entity class implicitly. |
| `build()` | Returns the fully composited `Specification<T>` ready to be passed to a repository. |

---

## 💻 Real-World Example

Let's look at how this simplifies a real-world use case inside `PropertyServiceImpl`, where properties are searched based on dynamic criteria like city, country, and capacity.

### ❌ Without `SpecificationBuilder` (The Old Way)

```java
public Specification<Property> buildSpec(PropertySearchCriteria request) {
    return (root, query, cb) -> {
        Predicate predicate = cb.conjunction();
        
        if (request.cityId() != null) {
            predicate = cb.and(predicate, propertySpecifications.withCity(request.cityId()).toPredicate(root, query, cb));
        }
        
        if (request.countryId() != null) {
            predicate = cb.and(predicate, propertySpecifications.withCountry(request.countryId()).toPredicate(root, query, cb));
        }
        
        if (request.adults() != null || request.childs() != null) {
            predicate = cb.and(predicate, propertySpecifications.withCapacity(request.adults(), request.childs()).toPredicate(root, query, cb));
        }

        return predicate;
    };
}
```

### ✅ With `SpecificationBuilder` (The Clean Way)

Using the custom builder, the same logic becomes exceptionally clean and declarative:

```java
@Override
public List<PropertyDto> searchProperty(PropertySearchCriteria request) {
    
    // 1. Build the specification dynamically
    Specification<Property> spec = new SpecificationBuilder<Property>()
            // Only applies if request.cityId() is NOT null
            .when(request.cityId(), propertySpecifications::withCity)
            
            // Only applies if request.countryId() is NOT null
            .when(request.countryId(), propertySpecifications::withCountry)
            
            // Applies if the boolean condition is true
            .when(request.adults() != null || request.childs() != null,
                    propertySpecifications.withCapacity(request.adults(), request.childs()))
            
            .build();

    // 2. Fetch from repository
    var properties = propertyRepository.findAll(spec);
    
    // 3. Map to DTOs
    return propertySummaryMapper.summary(properties);
}
```

### 🔍 Advanced Usage: Fetching and Identity Lookups

You can also handle conditional logic mixed with eager loading strategies effortlessly:

```java
@Override
public PropertyDto findPropertyById(Long propertyId, PropertySearchCriteria request) {

    var specs = new SpecificationBuilder<Property>()
            // Lookup by ID
            .whereId(propertyId, propertySpecifications::findPropertyById)
            
            // Optional conditional specifications
            .when(request.adults() != null || request.childs() != null,
                    propertySpecifications.withCapacity(request.adults(), request.childs()))
            
            // Unconditional specifications (pass 'true')
            .when(true, propertySpecifications.orderByCapacity())
            
            // Fix N+1 issues by eager loading entities
            .load(City.class) 
            
            .build();

    return propertyRepository.findOne(specs)
            .map(propertySummaryMapper::toSingleSummary)
            .orElseThrow(() -> new ResourcesNotFoundException(String.format("We could not find any property with Id %s", propertyId)));
}
```

## 🎯 Why Use This Pattern?

1. **Reduces Boilerplate**: Eliminates the manual underlying `CriteriaBuilder` API handling.
2. **Improves Readability**: At a glance, any developer can see exactly what queries dynamically apply to a model.
3. **Encourages Reusability**: Makes it extremely easy to reuse small, focused `Specification` components natively within a service layer.