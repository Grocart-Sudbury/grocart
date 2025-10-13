package com.grocart.grocart.Repository;


import com.grocart.grocart.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}