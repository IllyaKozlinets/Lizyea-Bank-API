package com.bankapp.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class TransferRequest {
    @NotNull
    UUID fromID;
    @NotNull
    UUID toID;
    @NotNull
    @Positive
    BigDecimal amount;

}
