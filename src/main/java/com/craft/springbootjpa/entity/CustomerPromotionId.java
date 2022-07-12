package com.craft.springbootjpa.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerPromotionId implements Serializable {

    @Column(name = "customer_id")
    private Long customerId;
    @Column(name = "promotion_id")
    private Long promotionId;

}
