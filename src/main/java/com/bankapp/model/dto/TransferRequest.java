package com.bankapp.model.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class TransferRequest {
    UUID fromID;
    UUID toID;
    BigDecimal amount;

    public UUID getFromID() {
        return fromID;
    }

    public UUID getToID() {
        return toID;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
