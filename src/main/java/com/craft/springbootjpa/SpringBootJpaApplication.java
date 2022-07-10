package com.craft.springbootjpa;

import com.craft.springbootjpa.entity.Customer;
import com.craft.springbootjpa.entity.Invoice;
import com.craft.springbootjpa.entity.Product;
import com.craft.springbootjpa.repository.CustomerRepository;
import com.craft.springbootjpa.entity.UserAccount;
import com.craft.springbootjpa.repository.ProductRepository;
import com.craft.springbootjpa.repository.UserAccountRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class SpringBootJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository,
                                        ProductRepository productRepository) {
        return args -> {

            fieldProductTable(productRepository);
            List<Product> products = productRepository.findAll();

            Customer customer = new Customer("Pedro", "Insua", "pedro@email.com",
                    LocalDate.now().minusYears(33));

            UserAccount userAccount = new UserAccount("userone", "34wefw4f34r34", Boolean.TRUE);
            userAccount.setCustomer(customer);
            customer.setUserAccount(userAccount);


            Invoice invoice = new Invoice(customer, LocalDateTime.now(), 566.0);
            invoice.setProducts(products.subList(0, 5).stream().collect(Collectors.toSet()));
            Invoice invoiceTwo = new Invoice(customer, LocalDateTime.now(), 888.3);
            invoiceTwo.setProducts(products.subList(6, 10).stream().collect(Collectors.toSet()));

            customer.setInvoices(List.of(invoice, invoiceTwo));

            customerRepository.save(customer);

            customerRepository.findAll().forEach(c -> System.out.println(c));

        };
    }

    void fieldProductTable(ProductRepository productRepository) {
        Faker faker = new Faker();
        for (int i = 0; i < 50; i++) {
            productRepository.save(new Product(faker.commerce().productName(),
                    faker.number().randomDouble(2, 1, 8000)));
        }
    }

}
