package com.craft.springbootjpa;

import com.craft.springbootjpa.entity.Customer;
import com.craft.springbootjpa.entity.CustomerPromotion;
import com.craft.springbootjpa.entity.CustomerPromotionId;
import com.craft.springbootjpa.entity.Invoice;
import com.craft.springbootjpa.entity.Product;
import com.craft.springbootjpa.entity.Promotion;
import com.craft.springbootjpa.entity.PromotionCode;
import com.craft.springbootjpa.entity.UserAccount;
import com.craft.springbootjpa.repository.CustomerRepository;
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
                                        PromotionRepository promotionRepository) {
        return args -> {

            //runJpaExamples(customerRepository, productRepository, promotionRepository);

        };
    }

    private void runJpaExamples(CustomerRepository customerRepository, ProductRepository productRepository, PromotionRepository promotionRepository) {
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

        Set<Product> productsInvoiceOne = products.subList(0, 5).stream().collect(Collectors.toSet());
        Invoice invoice = new Invoice(customer, LocalDateTime.now(),
                productsInvoiceOne.stream().mapToDouble(Product::getPrice).sum());
        invoice.setProducts(productsInvoiceOne);

        Set<Product> productsInvoiceTwo = products.subList(6, 10).stream().collect(Collectors.toSet());
        Invoice invoiceTwo = new Invoice(customer, LocalDateTime.now(),
                productsInvoiceTwo.stream().mapToDouble(Product::getPrice).sum());
        invoiceTwo.setProducts(productsInvoiceTwo);

        Set<Invoice> invoices = new HashSet<>();
        invoices.addAll(List.of(invoice, invoiceTwo));
        customer.setInvoices(invoices);

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

        //save promotions and update customers on cascade
        promotionRepository.saveAll(List.of(promotionOne, promotionTwo));

        System.out.println();
        System.out.println(" ----------------- Print all customers");
        customerRepository.findAll().forEach(c -> System.out.println(c));


        /**
         * JPA Query Methods example
         *
         * Note that to create this method to obtain the list of products by price greater than the long param, we just created
         * a method in ProductRepository with camel case syntax that the framework will translate to a JPQL query.
         */
        System.out.println();
        System.out.println(" ----------------- Query method examples");
        System.out.println(" ----------------- Products with prices greater than 5000");
        List<Product> greaterThanPriceProductList = productRepository.findProductsByPriceGreaterThan(Double.valueOf(5000));
        greaterThanPriceProductList.forEach(product -> System.out.println(product.getName() + " " + product.getPrice()));

        /**
         * JPQL example
         *
         * With JPA you can create your own queries in the language of JPQL
         *
         * This is the recommended why to create custom methods in your repository because you can clearly edit the
         * query when they are complex
         *
         */

        System.out.println();
        System.out.println(" ----------------- Products with prices greater than 7000 and less than 7500 and name length greater than 25");
        List<Product> priceBetweenAndNameEndingLikeProducts
                = productRepository.findProductsByNameLengthGreaterThanAndPriceBetween(23,
                Double.valueOf(3000),Double.valueOf(7500));
        priceBetweenAndNameEndingLikeProducts.forEach(product -> System.out.println(product.getName() + " "
                + product.getPrice() + " - Name length: " +  product.getName().length()));

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
        System.out.println();
        System.out.println(" ----------------- Page example");
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
         * Native SQL queries example.
         *
         * You can create a method in your repository where apply a SQL native query to database.
         *
         * This is not a recommended practice, but can be useful in certain circumstances.
         *
         */
        System.out.println();
        System.out.println(" ----------------- Customer with at least one invoice and age greater than 25");
        List<Customer> customersFound = customerRepository.findCustomersWithAtLestOneInvoiceAndAgeGreaterThan(25);
        customersFound.forEach(c -> System.out.println(c.getFirstName() + " "
                + c.getInvoices().size() + " age: "
                + (LocalDate.now().getYear() - c.getBirthDate().getYear())));

        customersFound.get(0).getInvoices().forEach(System.out::println);
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
