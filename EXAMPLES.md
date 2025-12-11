# Query Examples with Hibernate Session

This document provides detailed examples of how to query data using Hibernate Session obtained from EntityManager in a Spring Boot application.

## Getting Hibernate Session from EntityManager

The key concept is to use `EntityManager.unwrap(Session.class)` to obtain a Spring-managed Hibernate Session:

```java
@Repository
@Transactional
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
```

**Important Notes:**
- Use `@PersistenceContext` (not `@Autowired`) to inject EntityManager
- Always use `@Transactional` on repository methods that modify data
- The `unwrap()` method is the modern, standard way to access underlying implementations

## Query Examples

### 1. Simple Select Query (Find All)

```java
public List<User> findAll() {
    Session session = getSession();
    Query<User> query = session.createQuery("FROM User", User.class);
    return query.getResultList();
}
```

**HQL:** `FROM User` selects all User entities.

### 2. Query with Parameters (Parameterized Query)

```java
public List<User> findByName(String name) {
    Session session = getSession();
    Query<User> query = session.createQuery(
        "FROM User u WHERE u.name = :name", 
        User.class
    );
    query.setParameter("name", name);
    return query.getResultList();
}
```

**Why use parameters?**
- Prevents SQL injection attacks
- Allows query plan caching
- Type-safe parameter binding

### 3. Query with Comparison Operators

```java
public List<User> findUsersOlderThan(Integer age) {
    Session session = getSession();
    Query<User> query = session.createQuery(
        "FROM User u WHERE u.age > :age ORDER BY u.age DESC", 
        User.class
    );
    query.setParameter("age", age);
    return query.getResultList();
}
```

**Features:**
- Greater than comparison: `u.age > :age`
- Ordering results: `ORDER BY u.age DESC`

### 4. Single Result Query

```java
public Optional<User> findByEmail(String email) {
    Session session = getSession();
    Query<User> query = session.createQuery(
        "FROM User u WHERE u.email = :email", 
        User.class
    );
    query.setParameter("email", email);
    return query.uniqueResultOptional();
}
```

**Methods for single results:**
- `uniqueResultOptional()` - Returns Optional<T>, null-safe
- `uniqueResult()` - Returns T or null
- `getSingleResult()` - Throws exception if no result

### 5. Pattern Matching (LIKE Query)

```java
public List<User> findByNameAndMinAge(String name, Integer minAge) {
    Session session = getSession();
    Query<User> query = session.createQuery(
        "FROM User u WHERE u.name LIKE :name AND u.age >= :minAge", 
        User.class
    );
    query.setParameter("name", "%" + name + "%");
    query.setParameter("minAge", minAge);
    return query.getResultList();
}
```

**Pattern matching:**
- `LIKE` operator for partial matches
- `%` wildcard for any characters
- Multiple conditions with `AND`/`OR`

### 6. Pagination

```java
public List<User> findAllWithPagination(int pageNumber, int pageSize) {
    Session session = getSession();
    Query<User> query = session.createQuery(
        "FROM User u ORDER BY u.id", 
        User.class
    );
    query.setFirstResult(pageNumber * pageSize);
    query.setMaxResults(pageSize);
    return query.getResultList();
}
```

**Pagination methods:**
- `setFirstResult(offset)` - Skip first N results
- `setMaxResults(limit)` - Return at most N results

### 7. Aggregation Query (Count)

```java
public Long count() {
    Session session = getSession();
    Query<Long> query = session.createQuery(
        "SELECT COUNT(u) FROM User u", 
        Long.class
    );
    return query.getSingleResult();
}
```

**Other aggregate functions:**
- `COUNT()` - Count rows
- `SUM()` - Sum values
- `AVG()` - Average
- `MAX()`, `MIN()` - Maximum/minimum

### 8. Native SQL Query

```java
public List<User> findAllUsingNativeSQL() {
    Session session = getSession();
    @SuppressWarnings("unchecked")
    Query<User> query = session.createNativeQuery(
        "SELECT * FROM users", 
        User.class
    );
    return query.getResultList();
}
```

**When to use native SQL:**
- Database-specific features
- Complex joins not easily expressed in HQL
- Performance optimization with specific SQL

### 9. Basic CRUD Operations

#### Create/Save
```java
public User save(User user) {
    Session session = getSession();
    session.save(user);
    return user;
}
```

#### Read/Find
```java
public Optional<User> findById(Long id) {
    Session session = getSession();
    User user = session.get(User.class, id);
    return Optional.ofNullable(user);
}
```

#### Update
```java
public User update(User user) {
    Session session = getSession();
    session.update(user);
    return user;
}
```

#### Delete
```java
public void deleteById(Long id) {
    Session session = getSession();
    User user = session.get(User.class, id);
    if (user != null) {
        session.delete(user);
    }
}
```

## HQL vs Native SQL

### HQL (Hibernate Query Language)
```java
// HQL - works with entities
"FROM User u WHERE u.age > :age"
```

**Advantages:**
- Database-independent
- Works with entity names and properties
- Type-safe
- Easier to maintain

### Native SQL
```java
// Native SQL - works with tables
"SELECT * FROM users WHERE age > ?"
```

**Advantages:**
- Full database feature access
- Can be more performant for complex queries
- Easier for database-specific optimizations

## Best Practices

### 1. Always Use Parameter Binding
❌ **Bad:**
```java
Query query = session.createQuery(
    "FROM User u WHERE u.name = '" + name + "'"
);
```

✅ **Good:**
```java
Query<User> query = session.createQuery(
    "FROM User u WHERE u.name = :name", 
    User.class
);
query.setParameter("name", name);
```

### 2. Use Appropriate Result Methods
- Use `getResultList()` for multiple results
- Use `uniqueResultOptional()` for single result (null-safe)
- Use `getSingleResult()` only when you're sure there's exactly one result

### 3. Always Close Sessions (Handled by Spring)
When using Spring-managed sessions, you don't need to manually close them. Spring handles session lifecycle within transactions.

### 4. Use @Transactional
Always annotate repository methods with `@Transactional`:
```java
@Repository
@Transactional
public class UserRepository {
    // methods...
}
```

### 5. Handle Optional Results
```java
// Good practice
Optional<User> userOpt = findByEmail(email);
if (userOpt.isPresent()) {
    User user = userOpt.get();
    // process user
}
```

## Common Pitfalls to Avoid

1. **SQL Injection** - Never concatenate user input into queries
2. **N+1 Query Problem** - Use JOIN FETCH for associations
3. **Session Leak** - Let Spring manage session lifecycle
4. **Transaction Boundaries** - Keep transactions as short as possible
5. **Lazy Loading Issues** - Be aware of lazy-loaded associations outside transactions

## Testing Your Queries

Example test structure:

```java
@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByName() {
        // Given
        User user = new User("John Doe", "john@example.com", 25);
        userRepository.save(user);
        
        // When
        List<User> users = userRepository.findByName("John Doe");
        
        // Then
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
    }
}
```

## Further Reading

- [Hibernate Session Documentation](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#sessions)
- [HQL Language Reference](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#hql)
- [Spring Transaction Management](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction)
