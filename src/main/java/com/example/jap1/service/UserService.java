package com.example.jap1.service;

import com.example.jap1.entity.User;
import com.example.jap1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByName(String name) {
        return userRepository.findByName(name);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getUsersOlderThan(Integer age) {
        return userRepository.findUsersOlderThan(age);
    }

    public User updateUser(User user) {
        return userRepository.update(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Long countUsers() {
        return userRepository.count();
    }

    public List<User> getAllUsersWithNativeSQL() {
        return userRepository.findAllUsingNativeSQL();
    }

    public List<User> getUsersWithPagination(int pageNumber, int pageSize) {
        return userRepository.findAllWithPagination(pageNumber, pageSize);
    }

    public List<User> searchUsersByNameAndAge(String name, Integer minAge) {
        return userRepository.findByNameAndMinAge(name, minAge);
    }
}
