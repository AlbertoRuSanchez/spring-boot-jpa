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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Invoice")
@Table(name = "invoice")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Invoice {

    @Id
    @SequenceGenerator(
            name = "invoice_id_sequence",
            sequenceName = "invoice_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "invoice_id_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "customer_id",
            referencedColumnName = "id"
    )
    @ToString.Exclude
    private Customer customer;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "amount")
    private Double amount;

    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "invoice_product",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();

    public Invoice(Customer customer, LocalDateTime createdAt, Double amount) {
        this.customer = customer;
        this.createdAt = createdAt;
        this.amount = amount;
    }
}
