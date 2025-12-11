package com.example.jap1.repository;

import com.example.jap1.entity.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * Repository demonstrating how to use getSpringManagedHibernateSession()
 * to execute queries with Hibernate Session API
 */
@Repository
@Transactional
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Get the Hibernate Session from EntityManager
     */
    private Session getSession() {
        // This is how you get a Spring-managed Hibernate Session
        return entityManager.unwrap(Session.class);
    }

    /**
     * Save a user using Hibernate Session
     */
    public User save(User user) {
        Session session = getSession();
        session.save(user);
        return user;
    }

    /**
     * Find user by ID using Hibernate Session
     */
    public Optional<User> findById(Long id) {
        Session session = getSession();
        User user = session.get(User.class, id);
        return Optional.ofNullable(user);
    }

    /**
     * Find all users using HQL (Hibernate Query Language)
     */
    public List<User> findAll() {
        Session session = getSession();
        Query<User> query = session.createQuery("FROM User", User.class);
        return query.getResultList();
    }

    /**
     * Find users by name using HQL with parameter binding
     */
    public List<User> findByName(String name) {
        Session session = getSession();
        Query<User> query = session.createQuery("FROM User u WHERE u.name = :name", User.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    /**
     * Find users by email using HQL
     */
    public Optional<User> findByEmail(String email) {
        Session session = getSession();
        Query<User> query = session.createQuery("FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.uniqueResultOptional();
    }

    /**
     * Find users older than a certain age using HQL
     */
    public List<User> findUsersOlderThan(Integer age) {
        Session session = getSession();
        Query<User> query = session.createQuery(
            "FROM User u WHERE u.age > :age ORDER BY u.age DESC", 
            User.class
        );
        query.setParameter("age", age);
        return query.getResultList();
    }

    /**
     * Update user using Hibernate Session
     */
    public User update(User user) {
        Session session = getSession();
        session.update(user);
        return user;
    }

    /**
     * Delete user by ID using Hibernate Session
     */
    public void deleteById(Long id) {
        Session session = getSession();
        User user = session.get(User.class, id);
        if (user != null) {
            session.delete(user);
        }
    }

    /**
     * Count all users using HQL
     */
    public Long count() {
        Session session = getSession();
        Query<Long> query = session.createQuery("SELECT COUNT(u) FROM User u", Long.class);
        return query.getSingleResult();
    }

    /**
     * Example of native SQL query using Hibernate Session
     */
    public List<User> findAllUsingNativeSQL() {
        Session session = getSession();
        @SuppressWarnings("unchecked")
        Query<User> query = session.createNativeQuery("SELECT * FROM users", User.class);
        return query.getResultList();
    }

    /**
     * Find users with pagination using HQL
     */
    public List<User> findAllWithPagination(int pageNumber, int pageSize) {
        Session session = getSession();
        Query<User> query = session.createQuery("FROM User u ORDER BY u.id", User.class);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    /**
     * Example of named parameter query
     */
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
}
