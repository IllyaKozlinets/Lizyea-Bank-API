package com.bankapp.model.dto;
import com.bankapp.model.entity.enums.Currency;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


 @Setter
 @Getter
 public class CreateAccountRequest {
    private UUID userId;
    private Currency currency;
 }
