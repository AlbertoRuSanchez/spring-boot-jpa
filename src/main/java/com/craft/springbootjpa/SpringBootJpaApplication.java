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
import com.craft.springbootjpa.repository.PromotionCodeRepository;
import com.craft.springbootjpa.repository.PromotionRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
                                        PromotionCodeRepository promotionCodeRepository,
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
