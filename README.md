# How to Pass Table Names in Hibernate/JPA

This project demonstrates various ways to specify custom table names in Hibernate/JPA applications.

## Table of Contents
- [Overview](#overview)
- [Methods to Specify Table Names](#methods-to-specify-table-names)
- [Project Structure](#project-structure)
- [Running the Application](#running-the-application)
- [Testing the APIs](#testing-the-apis)

## Overview

In Hibernate/JPA, by default, the entity class name is used as the table name. However, you can customize this behavior using the `@Table` annotation.

## Methods to Specify Table Names

### 1. Basic @Table Annotation

The simplest way to specify a custom table name:

```java
@Entity
@Table(name = "users")
public class User {
    // entity fields
}
```

This maps the `User` entity to a database table named `users`.

**Example:** See `User.java` entity

### 2. @Table with Schema

You can also specify the schema along with the table name:

```java
@Entity
@Table(name = "products", schema = "inventory")
public class Product {
    // entity fields
}
```

This maps the entity to `inventory.products` table.

**Example:** See `Product.java` entity

### 3. @Table with Catalog and Schema

For databases that support catalogs:

```java
@Entity
@Table(
    name = "table_name",
    catalog = "catalog_name",
    schema = "schema_name"
)
public class MyEntity {
    // entity fields
}
```

### 4. Column Name Mapping

Along with table names, you can also customize column names:

```java
@Column(name = "user_name", nullable = false, length = 100)
private String userName;
```

## Project Structure

```
src/main/java/com/example/jap1/
├── entity/
│   ├── User.java          # Demonstrates basic @Table usage
│   └── Product.java       # Demonstrates @Table with schema
├── repository/
│   ├── UserRepository.java
│   └── ProductRepository.java
├── service/
│   ├── UserService.java
│   └── ProductService.java
├── controller/
│   ├── UserController.java
│   └── ProductController.java
└── Jap1Application.java
```

## Running the Application

1. **Build the project:**
   ```bash
   ./mvnw clean install
   ```

2. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access H2 Console (optional):**
   - URL: http://localhost:8080/h2-console
   - JDBC URL: jdbc:h2:mem:testdb
   - Username: sa
   - Password: (leave empty)

## Testing the APIs

### User API

**Create a User:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "John Doe",
    "email": "john@example.com",
    "age": 30
  }'
```

**Get All Users:**
```bash
curl http://localhost:8080/api/users
```

**Get User by ID:**
```bash
curl http://localhost:8080/api/users/1
```

**Delete User:**
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

### Product API

**Create a Product:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "productName": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99,
    "quantity": 50
  }'
```

**Get All Products:**
```bash
curl http://localhost:8080/api/products
```

**Get Product by ID:**
```bash
curl http://localhost:8080/api/products/1
```

**Delete Product:**
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

## Key Points

1. **@Entity** annotation marks a class as a JPA entity
2. **@Table** annotation specifies the table name (and optionally schema/catalog)
3. **@Column** annotation customizes column names and properties
4. **@Id** annotation marks the primary key field
5. **@GeneratedValue** annotation configures primary key generation strategy

## Database Schema

When you run the application, Hibernate will automatically create the following tables based on the entity definitions:

- `users` table with columns: id, user_name, email, age
- `inventory.products` table with columns: id, product_name, description, price, quantity

The SQL DDL statements will be visible in the console logs when `spring.jpa.show-sql=true` is configured.
