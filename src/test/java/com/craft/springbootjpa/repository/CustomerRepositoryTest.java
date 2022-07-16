package com.craft.springbootjpa.repository;

import com.craft.springbootjpa.entity.Customer;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
public class CustomerRepositoryTest {

    private static final String CUSTOMER_FIRSTNAME = "Pedro";
    private static final String CUSTOMER_LASTNAME = "Anguita";
    private static final String CUSTOMER_EMAIL = CUSTOMER_FIRSTNAME + "." + CUSTOMER_LASTNAME + "@email.com";
    private static final LocalDate CUSTOMER_BIRTHDATE = LocalDate.now().minusYears(50);

    @Autowired
    private CustomerRepository underTest;

    private Faker faker = new Faker();

    @Test
    void itShouldSaveCustomer() {
        //Given
        Customer customer = createCustomer();

        //When
        underTest.save(customer);

        //Then
        Optional<Customer> customerFromDataBase = underTest.findById(customer.getId());
        assertThat(customerFromDataBase.isPresent()).isTrue();
        assertThat(customerFromDataBase.get()).isNotNull();
        assertThat(customerFromDataBase).isPresent().hasValueSatisfying(customer1 -> {
            assertThat(customer.getFirstName()).isEqualTo(CUSTOMER_FIRSTNAME);
            assertThat(customer.getLastName()).isEqualTo(CUSTOMER_LASTNAME);
            assertThat(customer.getBirthDate()).isEqualTo(CUSTOMER_BIRTHDATE);
        });
    }

    @Test
    void itShouldUpdateCustomer() {
        //Given
        Customer customer = createCustomer();
        underTest.save(customer);

        String changedName = "Name changed";
        customer.setFirstName(changedName);

        //When
        underTest.save(customer);

        //Then
        Optional<Customer> customerFromDataBase = underTest.findById(customer.getId());
        assertThat(customerFromDataBase.isPresent()).isTrue();
        assertThat(customerFromDataBase.get()).isNotNull();
        assertThat(customerFromDataBase.get().getFirstName()).isEqualTo(changedName);
    }

    @Test
    void itShouldDeleteCustomer() {
        //Given
        Customer customer = createCustomer();
        underTest.save(customer);

        //When
        underTest.delete(customer);

        //Then
        Optional<Customer> customerOptional = underTest.findById(customer.getId());
        assertThat(customerOptional).isEmpty();
    }

    @Test
    void itShouldFindAllCustomersInTableCustomer() {
        //Given
        int numberOfCustomers = 100;
        fieldCustomerTableWithRandom(numberOfCustomers);

        //When
        List<Customer> customerList = underTest.findAll();

        //Then
        assertThat(customerList).hasSize(numberOfCustomers);

    }

    private Customer createCustomer(){
        return new Customer(CUSTOMER_FIRSTNAME, CUSTOMER_LASTNAME, CUSTOMER_EMAIL, CUSTOMER_BIRTHDATE);
    }

    private void fieldCustomerTableWithRandom(int numberOfCustomers){
        for (int i = 0; i < numberOfCustomers; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            Customer customer = new Customer(firstName, lastName, firstName + "." + lastName + "@mail.com",
                    LocalDate.now().minusYears(faker.number().randomDigit()));
            underTest.save(customer);
        }
    }
}
