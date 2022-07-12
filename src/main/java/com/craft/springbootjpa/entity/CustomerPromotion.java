package com.craft.springbootjpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity(name = "CustomerPromotion")
@Table(name = "customer_promotion")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CustomerPromotion {

    @EmbeddedId
    private CustomerPromotionId customerPromotionId;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "promotion_customer_id_fk"))
    @ToString.Exclude
    private Customer customer;

    @ManyToOne
    @MapsId("promotionId")
    @JoinColumn(name = "promotion_id", foreignKey = @ForeignKey(name = "promotion_code_id_fk"))
    @ToString.Exclude
    private Promotion promotion;

    public CustomerPromotion(CustomerPromotionId customerPromotionId) {
        this.customerPromotionId = customerPromotionId;
    }
    public CustomerPromotion(CustomerPromotionId customerPromotionId, Customer customer, Promotion promotion) {
        this.customerPromotionId = customerPromotionId;
        this.customer = customer;
        this.promotion = promotion;
    }
}
