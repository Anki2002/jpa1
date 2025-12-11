package com.example.jap1.entity;

import javax.persistence.*;

/**
 * Entity demonstrating basic @Table annotation usage
 * The @Table annotation is used to specify the table name in the database
 */
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_name", nullable = false, length = 100)
    private String userName;
    
    @Column(name = "email", unique = true)
    private String email;
    
    @Column(name = "age")
    private Integer age;
    
    // Constructors
    public User() {
    }
    
    public User(String userName, String email, Integer age) {
        this.userName = userName;
        this.email = email;
        this.age = age;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
}
