package com.craft.springbootjpa.repository;

import com.craft.springbootjpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
