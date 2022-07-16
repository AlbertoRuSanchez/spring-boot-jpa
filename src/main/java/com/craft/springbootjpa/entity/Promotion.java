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
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Promotion")
@Table(name = "promotion")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "name")
    private String name;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "valid_until")
    private LocalDate validUntil;

    @OneToMany(mappedBy = "promotion", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<CustomerPromotion> customerPromotions = new HashSet<>();

    @OneToMany(mappedBy = "promotion" , fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PromotionCode> promotionCodes = new HashSet<>();

    public Promotion(String name, LocalDate createdAt, LocalDate validUntil) {
        this.name = name;
        this.createdAt = createdAt;
        this.validUntil = validUntil;
    }
}
