package com.bankapp.model.entity.enums;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum TransactionStatus {
    @Enumerated(EnumType.STRING)
    PENDING,
    SUCCESS,
    FAILED
}
