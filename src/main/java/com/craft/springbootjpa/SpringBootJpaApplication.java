package com.craft.springbootjpa;

import com.craft.springbootjpa.entity.Customer;
import com.craft.springbootjpa.entity.CustomerPromotion;
import com.craft.springbootjpa.entity.Invoice;
import com.craft.springbootjpa.entity.Product;
import com.craft.springbootjpa.entity.Promotion;
import com.craft.springbootjpa.entity.PromotionCode;
import com.craft.springbootjpa.entity.CustomerPromotionId;
import com.craft.springbootjpa.repository.CustomerRepository;
import com.craft.springbootjpa.entity.UserAccount;
import com.craft.springbootjpa.repository.InvoiceRepository;
import com.craft.springbootjpa.repository.ProductRepository;
import com.craft.springbootjpa.repository.PromotionRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootApplication
public class SpringBootJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaApplication.class, args);
    }

    private Faker faker = new Faker();

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository,
                                        ProductRepository productRepository,
                                        InvoiceRepository invoiceRepository,
                                        PromotionRepository promotionRepository) {
        return args -> {

            /**
             * Field with random values the tables
             */
            fieldProductTable(productRepository);
            List<Product> products = productRepository.findAll();

            fieldCustomerTable(customerRepository);
            List<Customer> customers = customerRepository.findAll();
            Customer customer = customers.get(0);

            /**
             * Example use of Pageable
             *
             * First, you can know the number of pages based on the number of line per page. The might change from, for
             * example, a configurable filter and sorting datatable at frontend
             *
             * Once the table is loaded with the product per page and number of pages params, every page of the table will
             * consume a pageable from the backend.
             *
             * We can add easily different params to sort by specific columns or direction (ascending or descending)
             *
             */

            Sort sort = Sort.by("price").descending();
            Pageable firstPageableProducts = PageRequest.of(0, 6, sort);
            Pageable secondPageableProducts = PageRequest.of(1, 6, sort);
            Pageable thirdPageableProducts = PageRequest.of(2, 6, sort);

            Long numberOfProducts = productRepository.count();
            Integer productsPerPage = 6;
            Integer numberOfPages = Double.valueOf(numberOfProducts / productsPerPage).intValue();
            System.out.println("The number of pages of products is : " + numberOfPages);

            Page<Product> firstPage = productRepository.findAll(firstPageableProducts);
            System.out.println();
            System.out.println("Page 0:");
            firstPage.forEach(product -> System.out.println(product.getName() + " -> " + product.getPrice()));
            Page<Product> secondPage = productRepository.findAll(secondPageableProducts);
            System.out.println();
            System.out.println("Page 1:");
            secondPage.forEach(product -> System.out.println(product.getName() + " -> " + product.getPrice()));
            Page<Product> thirdPage = productRepository.findAll(thirdPageableProducts);
            System.out.println();
            System.out.println("Page 2:");
            thirdPage.forEach(product -> System.out.println(product.getName() + " -> " + product.getPrice()));

            /**
             * Adding an account with OneToOne relationship
             */
            UserAccount userAccount = new UserAccount(customer.getEmail(), faker.random().hex(8), Boolean.TRUE);
            userAccount.setCustomer(customer);
            customer.setUserAccount(userAccount);
            customerRepository.save(customer);


            /**
             * Adding several invoices to a customer with OneToMany relationship
             *
             * At the same time, we add on cascade, the invoices lines in a ManyToMany direct relationship with products
             */
            Invoice invoice = new Invoice(customer, LocalDateTime.now(), 566.0);
            invoice.setProducts(products.subList(0, 5).stream().collect(Collectors.toSet()));
            Invoice invoiceTwo = new Invoice(customer, LocalDateTime.now(), 888.3);
            invoiceTwo.setProducts(products.subList(6, 10).stream().collect(Collectors.toSet()));
            invoiceRepository.saveAll(List.of(invoice, invoiceTwo));
            customer.setInvoices(List.of(invoice, invoiceTwo));
            customerRepository.save(customer);

            /**
             * Adding several promotions.
             *
             * Promotion is the parent Entity. We create a Promotion, set list of PromotionCodes and List of CustomerPromotions
             *
             * Just save the promotion, JPA will persist the child entities by cascade.
             *
             */
            Promotion promotionOne = new Promotion("Spring promo" , LocalDate.now(),
                    LocalDate.now().plusMonths(5));

            Promotion promotionTwo = new Promotion("Winter promo" , LocalDate.now(),
                    LocalDate.now().plusMonths(2));

            //create the codes
            Set<PromotionCode> promotionCodesOne = new HashSet<>();
            Set<PromotionCode> promotionCodesTwo = new HashSet<>();
            for (int i = 0; i < 20; i++) {
                promotionCodesOne.add(new PromotionCode(faker.commerce().promotionCode()));
                promotionCodesTwo.add(new PromotionCode(faker.commerce().promotionCode()));
            }
            promotionOne.setPromotionCodes(promotionCodesOne);
            promotionTwo.setPromotionCodes(promotionCodesTwo);
            promotionRepository.saveAll(List.of(promotionOne, promotionTwo));

            //assign the lucky customers to the promotion
            Set<CustomerPromotion> customerPromotions = new HashSet<>();
            customers.subList(0,5).forEach(c -> {
                customerPromotions.add(new CustomerPromotion(new CustomerPromotionId(c.getId(), promotionOne.getId()), c, promotionOne));
                customerPromotions.add(new CustomerPromotion(new CustomerPromotionId(c.getId(), promotionTwo.getId()), c, promotionTwo));
            });
            promotionOne.setCustomerPromotions(customerPromotions);
            promotionRepository.saveAll(List.of(promotionOne, promotionTwo));


            customerRepository.findAll().forEach(c -> System.out.println(c));

        };
    }

    void fieldProductTable(ProductRepository productRepository) {
        for (int i = 0; i < 50; i++) {
            productRepository.save(new Product(faker.commerce().productName(),
                    faker.number().randomDouble(2, 1, 8000)));
        }
    }

    void fieldCustomerTable(CustomerRepository customerRepository){
        for (int i = 0; i < 50; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            customerRepository.save(new Customer(firstName, lastName, firstName + "." + lastName + "@mail.com",
                    LocalDate.now().minusYears(faker.number().randomDigit())));
        }
    }
}
