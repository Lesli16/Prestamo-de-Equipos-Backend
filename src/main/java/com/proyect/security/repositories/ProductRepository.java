package com.proyect.security.repositories;

import com.proyect.security.entities.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
    
    
}
