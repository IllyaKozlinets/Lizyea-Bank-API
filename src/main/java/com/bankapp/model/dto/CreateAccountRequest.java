package com.bankapp.model.dto;
import com.bankapp.model.entity.enums.Currency;

import java.util.UUID;


 public class CreateAccountRequest {
    private UUID userId;
    private Currency currency;

     public UUID getUserId() {
         return userId;
     }

     public void setUserId(UUID userId) {
         this.userId = userId;
     }

     public Currency getCurrency() {
         return currency;
     }

     public void setCurrency(Currency currency) {
         this.currency = currency;
     }
 }
