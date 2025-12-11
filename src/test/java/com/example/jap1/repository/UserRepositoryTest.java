package com.example.jap1.repository;

import com.example.jap1.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser1;
    private User testUser2;
    private User testUser3;

    @BeforeEach
    void setUp() {
        testUser1 = new User("John Doe", "john@example.com", 25);
        testUser2 = new User("Jane Smith", "jane@example.com", 30);
        testUser3 = new User("John Smith", "johnsmith@example.com", 35);
    }

    @Test
    void testSaveUser() {
        User savedUser = userRepository.save(testUser1);
        
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals(25, savedUser.getAge());
    }

    @Test
    void testFindById() {
        User savedUser = userRepository.save(testUser1);
        
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
        assertEquals("John Doe", foundUser.get().getName());
    }

    @Test
    void testFindAll() {
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);
        
        List<User> users = userRepository.findAll();
        
        assertNotNull(users);
        assertEquals(3, users.size());
    }

    @Test
    void testFindByName() {
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);
        
        List<User> users = userRepository.findByName("John Doe");
        
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
    }

    @Test
    void testFindByEmail() {
        userRepository.save(testUser1);
        
        Optional<User> foundUser = userRepository.findByEmail("john@example.com");
        
        assertTrue(foundUser.isPresent());
        assertEquals("john@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindUsersOlderThan() {
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);
        
        List<User> users = userRepository.findUsersOlderThan(28);
        
        assertNotNull(users);
        assertEquals(2, users.size());
        // Should be ordered by age descending
        assertEquals(35, users.get(0).getAge());
        assertEquals(30, users.get(1).getAge());
    }

    @Test
    void testUpdateUser() {
        User savedUser = userRepository.save(testUser1);
        
        savedUser.setName("John Updated");
        savedUser.setAge(26);
        User updatedUser = userRepository.update(savedUser);
        
        assertEquals("John Updated", updatedUser.getName());
        assertEquals(26, updatedUser.getAge());
    }

    @Test
    void testDeleteById() {
        User savedUser = userRepository.save(testUser1);
        Long userId = savedUser.getId();
        
        userRepository.deleteById(userId);
        
        Optional<User> deletedUser = userRepository.findById(userId);
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void testCount() {
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);
        
        Long count = userRepository.count();
        
        assertEquals(3L, count);
    }

    @Test
    void testFindAllUsingNativeSQL() {
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        
        List<User> users = userRepository.findAllUsingNativeSQL();
        
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    void testFindAllWithPagination() {
        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);
        
        // Get first page with 2 items
        List<User> page1 = userRepository.findAllWithPagination(0, 2);
        assertEquals(2, page1.size());
        
        // Get second page with 2 items
        List<User> page2 = userRepository.findAllWithPagination(1, 2);
        assertEquals(1, page2.size());
    }

    @Test
    void testFindByNameAndMinAge() {
        userRepository.save(testUser1); // John Doe, age 25
        userRepository.save(testUser2); // Jane Smith, age 30
        userRepository.save(testUser3); // John Smith, age 35
        
        // Search for users with name containing "John" and age >= 30
        List<User> users = userRepository.findByNameAndMinAge("John", 30);
        
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("John Smith", users.get(0).getName());
        assertEquals(35, users.get(0).getAge());
    }
}
