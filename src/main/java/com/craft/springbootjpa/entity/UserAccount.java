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

@Entity(name = "UserAccount")
@Table(name = "user_account")
@Getter
@Setter
@NoArgsConstructor
public class UserAccount {

    @Id
    @SequenceGenerator(
            name = "user_id_sequence",
            sequenceName = "user_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence")
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
    private Customer customer;

    public UserAccount(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
