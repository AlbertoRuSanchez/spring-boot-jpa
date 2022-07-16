package com.craft.springbootjpa.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

@Entity(name = "UserAccount")
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;
    @Column (name = "user_name")
    private String userName;
    @Column(name = "password", nullable = false)
    private String password;

    @Column (name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn (name = "customer_id", referencedColumnName = "id")
    @ToString.Exclude
    private Customer customer;

    public UserAccount(String userName, String password, Boolean isActive) {
        this.userName = userName;
        this.password = password;
        this.isActive = isActive;
    }


}
