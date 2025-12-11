# Spring JPA with Hibernate Session Query Examples

This project demonstrates how to query data using `getSpringManagedHibernateSession()` (or `EntityManager.unwrap(Session.class)`) in a Spring Boot JPA application.

## Overview

When working with Spring Data JPA, you typically use `EntityManager` for database operations. However, sometimes you need direct access to Hibernate's Session API for advanced features or specific query requirements. This project shows how to properly obtain and use a Spring-managed Hibernate Session.

## Getting the Hibernate Session

The key method to get a Hibernate Session from EntityManager is:

```java
@PersistenceContext
private EntityManager entityManager;

private Session getSession() {
    return entityManager.unwrap(Session.class);
}
```

**Note:** In older versions of Hibernate/JPA, there was a method called `getDelegate()` that could be used, but the modern and recommended approach is to use `unwrap(Session.class)`.

## Project Structure

```
src/main/java/com/example/jap1/
├── entity/
│   └── User.java                 # JPA Entity
├── repository/
│   └── UserRepository.java       # Repository with Hibernate Session examples
├── service/
│   └── UserService.java          # Service layer
├── controller/
│   └── UserController.java       # REST API endpoints
└── Jap1Application.java          # Spring Boot Application
```

## Key Features Demonstrated

### 1. Basic CRUD Operations with Hibernate Session

```java
// Save
Session session = getSession();
session.save(user);

// Find by ID
User user = session.get(User.class, id);

// Update
session.update(user);

// Delete
session.delete(user);
```

### 2. HQL (Hibernate Query Language) Queries

```java
// Simple query
Query<User> query = session.createQuery("FROM User", User.class);
List<User> users = query.getResultList();

// Query with parameters
Query<User> query = session.createQuery(
    "FROM User u WHERE u.name = :name", 
    User.class
);
query.setParameter("name", name);
List<User> users = query.getResultList();
```

### 3. Advanced Query Features

The `UserRepository` class demonstrates:

- **Parameter binding** - Safe SQL injection prevention
- **Sorting** - ORDER BY clauses in HQL
- **Filtering** - WHERE clauses with comparisons
- **Pagination** - Using `setFirstResult()` and `setMaxResults()`
- **Native SQL** - When you need raw SQL queries
- **Pattern matching** - LIKE queries for partial matches
- **Aggregations** - COUNT and other aggregate functions

### 4. Repository Methods

The `UserRepository` class includes these example methods:

| Method | Description | Query Type |
|--------|-------------|------------|
| `save(User)` | Save a new user | Session API |
| `findById(Long)` | Find user by ID | Session API |
| `findAll()` | Get all users | HQL |
| `findByName(String)` | Find users by exact name | HQL with parameters |
| `findByEmail(String)` | Find user by email | HQL with parameters |
| `findUsersOlderThan(Integer)` | Find users older than age | HQL with ORDER BY |
| `update(User)` | Update existing user | Session API |
| `deleteById(Long)` | Delete user by ID | Session API |
| `count()` | Count total users | HQL with aggregation |
| `findAllUsingNativeSQL()` | Get all users with SQL | Native SQL |
| `findAllWithPagination(int, int)` | Get users with pagination | HQL with pagination |
| `findByNameAndMinAge(String, Integer)` | Search by name and age | HQL with multiple params |

## Running the Application

### Prerequisites

- Java 17 or higher
- Maven

### Build and Run

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

### Run Tests

```bash
./mvnw test
```

## API Endpoints

Once the application is running, you can use these REST endpoints:

### Create User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","age":25}'
```

### Get All Users
```bash
curl http://localhost:8080/api/users
```

### Get User by ID
```bash
curl http://localhost:8080/api/users/1
```

### Search Users by Name
```bash
curl http://localhost:8080/api/users/search/name/John%20Doe
```

### Search Users by Email
```bash
curl http://localhost:8080/api/users/search/email/john@example.com
```

### Get Users Older Than Age
```bash
curl http://localhost:8080/api/users/search/age-greater-than/25
```

### Update User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"John Updated","email":"john@example.com","age":26}'
```

### Delete User
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

### Count Users
```bash
curl http://localhost:8080/api/users/count
```

### Get Users with Pagination
```bash
curl "http://localhost:8080/api/users/paginated?page=0&size=10"
```

### Search Users by Name and Age
```bash
curl "http://localhost:8080/api/users/search?name=John&minAge=25"
```

### Get Users Using Native SQL
```bash
curl http://localhost:8080/api/users/native
```

## H2 Console

The application includes H2 in-memory database with console enabled for debugging:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: *(leave empty)*

## Important Notes

### Transaction Management

All repository methods are annotated with `@Transactional` to ensure proper transaction management. This is crucial when using Hibernate Session:

```java
@Repository
@Transactional
public class UserRepository {
    // methods...
}
```

### EntityManager Injection

Use `@PersistenceContext` to inject EntityManager, not `@Autowired`:

```java
@PersistenceContext
private EntityManager entityManager;
```

This ensures you get a Spring-managed, transaction-aware EntityManager.

### Session vs EntityManager

- **Use Session when:** You need Hibernate-specific features, advanced caching, or specific query optimizations
- **Use EntityManager when:** Standard JPA operations are sufficient (recommended for portability)

## Dependencies

The project uses these key dependencies:

- **Spring Boot Starter Data JPA** - JPA support with Hibernate
- **Spring Boot Starter Web** - REST API support
- **H2 Database** - In-memory database for development
- **Spring Boot Starter Test** - Testing support

## Best Practices

1. **Always use parameter binding** - Never concatenate user input directly into queries
2. **Keep transactions short** - Open transaction, do work, commit/rollback
3. **Use @Transactional appropriately** - Ensure all database operations are within a transaction
4. **Prefer JPA criteria queries for dynamic queries** - More type-safe than string-based HQL
5. **Use projections when you don't need full entities** - Better performance
6. **Test your repository layer** - Include integration tests with actual database

## Additional Resources

- [Hibernate Session Documentation](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#sessions)
- [Spring Data JPA Documentation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [HQL Reference](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#hql)

## License

This project is created for educational purposes to demonstrate Hibernate Session usage in Spring Boot.
