package com.bankapp.model.dto;

import com.bankapp.model.entity.enums.TransactionStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionCreatedEvent(
        UUID transactionId,
        BigDecimal amount,
        UUID sender,
        UUID receiver,
        TransactionStatus status
) {
}
