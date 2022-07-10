package com.craft.springbootjpa.repository;

import com.craft.springbootjpa.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
