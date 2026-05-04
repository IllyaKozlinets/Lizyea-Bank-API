package com.bankapp.model.entity;

import com.bankapp.model.entity.enums.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Account {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private BigDecimal balance;
    private Currency currency;



    public Account(UUID id, User user, BigDecimal balance, Currency currency) {
        this.id = id;
        this.user = user;
        this.balance = balance;
        this.currency = currency;
    }
    public Account() {
    }

}
