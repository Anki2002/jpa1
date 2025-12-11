package com.example.jap1.service;

import com.example.jap1.entity.Product;
import com.example.jap1.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    
    public Product getProductByName(String name) {
        return productRepository.findByProductName(name);
    }
    
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
