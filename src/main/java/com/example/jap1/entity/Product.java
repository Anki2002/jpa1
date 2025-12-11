package com.example.jap1.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entity demonstrating @Table annotation with schema and catalog
 * This shows how to specify schema and catalog along with table name
 */
@Entity
@Table(
    name = "products",
    schema = "inventory"
    // catalog can also be specified: catalog = "catalog_name"
)
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_name", nullable = false)
    private String productName;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    // Constructors
    public Product() {
    }
    
    public Product(String productName, String description, BigDecimal price, Integer quantity) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
