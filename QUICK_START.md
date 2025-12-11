# Quick Start: How to Query with getSpringManagedHibernateSession

This is a quick reference guide answering: **"How to query while using getSpringManagedHibernateSession"**

## TL;DR - The Answer

To query using a Spring-managed Hibernate Session:

1. **Get the Session from EntityManager:**
```java
@PersistenceContext
private EntityManager entityManager;

Session session = entityManager.unwrap(Session.class);
```

2. **Create and Execute a Query:**
```java
Query<User> query = session.createQuery("FROM User WHERE age > :age", User.class);
query.setParameter("age", 25);
List<User> users = query.getResultList();
```

That's it! Now let's see a complete example.

## Complete Working Example

### Step 1: Create a Repository with @PersistenceContext

```java
package com.example.jap1.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Helper method to get Hibernate Session
    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    // Example: Query all users
    public List<User> findAll() {
        Session session = getSession();
        Query<User> query = session.createQuery("FROM User", User.class);
        return query.getResultList();
    }

    // Example: Query with parameters
    public List<User> findByName(String name) {
        Session session = getSession();
        Query<User> query = session.createQuery(
            "FROM User u WHERE u.name = :name", 
            User.class
        );
        query.setParameter("name", name);
        return query.getResultList();
    }
}
```

### Step 2: Use it in Your Service

```java
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByName(String name) {
        return userRepository.findByName(name);
    }
}
```

## Important Points

### ✅ DO's

1. **Use `@PersistenceContext`** (not `@Autowired`) for EntityManager:
```java
@PersistenceContext
private EntityManager entityManager;
```

2. **Use `@Transactional`** on your repository class:
```java
@Repository
@Transactional
public class UserRepository { ... }
```

3. **Always use parameter binding** to prevent SQL injection:
```java
query.setParameter("name", name);
```

4. **Use the correct query method**:
   - `getResultList()` - for multiple results
   - `uniqueResultOptional()` - for single result (returns Optional)
   - `getSingleResult()` - for single result (throws exception if none)

### ❌ DON'Ts

1. **Don't manually close the session** - Spring manages it
2. **Don't concatenate user input** into queries
3. **Don't use `@Autowired`** for EntityManager (use `@PersistenceContext`)

## Running the Example in This Project

1. **Start the application:**
```bash
./mvnw spring-boot:run
```

2. **Create a user:**
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","age":25}'
```

3. **Query users:**
```bash
# Get all users
curl http://localhost:8080/api/users

# Search by name
curl http://localhost:8080/api/users/search/name/John%20Doe

# Find users older than 20
curl http://localhost:8080/api/users/search/age-greater-than/20
```

## Common Query Patterns

### 1. Select All
```java
Query<User> query = session.createQuery("FROM User", User.class);
List<User> users = query.getResultList();
```

### 2. Where Clause
```java
Query<User> query = session.createQuery("FROM User WHERE age > 25", User.class);
List<User> users = query.getResultList();
```

### 3. With Parameters
```java
Query<User> query = session.createQuery("FROM User WHERE name = :name", User.class);
query.setParameter("name", "John");
List<User> users = query.getResultList();
```

### 4. Order By
```java
Query<User> query = session.createQuery("FROM User ORDER BY age DESC", User.class);
List<User> users = query.getResultList();
```

### 5. Multiple Conditions
```java
Query<User> query = session.createQuery(
    "FROM User WHERE age > :minAge AND name LIKE :name", 
    User.class
);
query.setParameter("minAge", 25);
query.setParameter("name", "%John%");
List<User> users = query.getResultList();
```

### 6. Pagination
```java
Query<User> query = session.createQuery("FROM User ORDER BY id", User.class);
query.setFirstResult(0);  // offset
query.setMaxResults(10);  // limit
List<User> users = query.getResultList();
```

### 7. Count
```java
Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u", Long.class);
Long count = query.getSingleResult();
```

### 8. Native SQL
```java
Query<User> query = session.createNativeQuery("SELECT * FROM users", User.class);
List<User> users = query.getResultList();
```

## Why Use Hibernate Session Instead of EntityManager?

You might want to use Hibernate Session when you need:

- **Hibernate-specific features** (second-level cache, filters, etc.)
- **Better control** over query execution
- **Advanced query capabilities** specific to Hibernate
- **Performance optimizations** available in Hibernate

For most cases, standard JPA EntityManager is sufficient. Use Session when you have specific requirements.

## Summary

The key to querying with Spring-managed Hibernate Session:

```java
// 1. Inject EntityManager with @PersistenceContext
@PersistenceContext
private EntityManager entityManager;

// 2. Get Session using unwrap()
Session session = entityManager.unwrap(Session.class);

// 3. Create query with HQL
Query<YourEntity> query = session.createQuery("FROM YourEntity", YourEntity.class);

// 4. Execute and get results
List<YourEntity> results = query.getResultList();
```

That's all you need! For more detailed examples, see `EXAMPLES.md` and `README.md` in this project.
