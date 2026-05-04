package com.bankapp.model.dto;

import com.bankapp.model.entity.enums.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountResponse {
    private UUID id;
    private Currency currency;
    private BigDecimal balance;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}


