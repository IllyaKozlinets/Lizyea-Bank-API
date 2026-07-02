package com.bankapp.model.dto;

import com.bankapp.model.entity.enums.Currency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
public class AccountResponse {

    @NotNull(message = "Account ID cannot be null")
    private UUID id;

    @NotNull(message = "Currency cannot be null")
    private Currency currency;

    @NotNull(message = "Balance cannot be null")
    @PositiveOrZero(message = "Balance must be zero or positive")
    private BigDecimal balance;
}


