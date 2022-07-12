package com.craft.springbootjpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Customer")
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Customer {
    @Id
    @SequenceGenerator(
            name = "customer_id_sequence",
            sequenceName = "customer_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_sequence"
    )
    @Column(name = "id", updatable = false)
    private Long id;
    @Column (name = "first_name", nullable = false)
    private String fistName;
    @Column (name = "last_name", nullable = false)
    private String lastName;
    @Column (name = "email", nullable = false, unique = true)
    private String email;
    @Column (name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @OneToOne (mappedBy = "customer", orphanRemoval = true, cascade = CascadeType.ALL)
    private UserAccount userAccount;

    @OneToMany (
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<Invoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private Set<CustomerPromotion> customerPromotions = new HashSet<>();

    public Customer(String fistName, String lastName, String email, LocalDate birthDate) {
        this.fistName = fistName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
    }
}
