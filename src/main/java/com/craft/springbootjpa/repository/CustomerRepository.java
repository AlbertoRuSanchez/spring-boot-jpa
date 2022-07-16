package com.craft.springbootjpa.repository;

import com.craft.springbootjpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(value = "SELECT DISTINCT customer.* " +
            "FROM customer " +
            "JOIN invoice ON customer.id = invoice.customer_id " +
            "WHERE EXTRACT(YEAR FROM birth_date) > ?1", nativeQuery = true)
    List<Customer> findCustomersWithAtLestOneInvoiceAndAgeGreaterThan(Integer age);

}
