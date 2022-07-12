package com.craft.springbootjpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "PromotionCode")
@Table(name = "promotion_code")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class PromotionCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "promotion_id" , referencedColumnName = "id")
    private Promotion promotion;

    public PromotionCode(String code) {
        this.code = code;
    }
}
