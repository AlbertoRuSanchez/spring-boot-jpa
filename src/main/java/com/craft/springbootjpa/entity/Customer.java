package com.craft.springbootjpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity(name = "Customer")
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
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

}
