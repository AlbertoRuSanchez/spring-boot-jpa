package com.craft.springbootjpa.repository;

import com.craft.springbootjpa.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findProductsByPriceGreaterThan(Double price);

    @Query("SELECT p FROM Product p WHERE LENGTH(p.name) > ?1 AND p.price BETWEEN ?2 AND ?3")
    List<Product> findProductsByNameLengthGreaterThanAndPriceBetween(Integer nameEnding, Double greaterThanPrice, Double lessThanPrice);

}
