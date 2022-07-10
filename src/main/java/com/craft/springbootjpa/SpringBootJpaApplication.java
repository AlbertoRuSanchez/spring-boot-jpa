package com.craft.springbootjpa;

import com.craft.springbootjpa.entity.Customer;
import com.craft.springbootjpa.repository.CustomerRepository;
import com.craft.springbootjpa.entity.UserAccount;
import com.craft.springbootjpa.repository.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class SpringBootJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository,
                                        UserAccountRepository userAccountRepository) {
        return args -> {

            Customer customer = new Customer("Pedro", "Insua", "pedro@email.com",
                    LocalDate.now().minusYears(33));
            customerRepository.saveAndFlush(customer);

            UserAccount userAccount = new UserAccount("userone", "34wefw4f34r34", Boolean.TRUE);

            userAccount.setCustomer(customer);
            customer.setUserAccount(userAccount);

            customerRepository.saveAndFlush(customer);

            customerRepository.findAll().forEach(c -> System.out.println(c));

        };
    }

}
