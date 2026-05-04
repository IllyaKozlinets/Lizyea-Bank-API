package com.bankapp.mapper;

import com.bankapp.model.dto.AccountResponse;
import com.bankapp.model.entity.Account;

public class AccountMapper {
    public static AccountResponse toResponse(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(account.getId());
        accountResponse.setBalance(account.getBalance());
        accountResponse.setCurrency(account.getCurrency());
        return accountResponse;
    }
}
