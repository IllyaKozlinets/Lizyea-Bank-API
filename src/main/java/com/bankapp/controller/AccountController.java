package com.bankapp.controller;

import com.bankapp.mapper.AccountMapper;
import com.bankapp.model.dto.AccountResponse;
import com.bankapp.model.dto.CreateAccountRequest;
import com.bankapp.model.entity.Account;
import com.bankapp.model.entity.enums.Currency;
import com.bankapp.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/accounts/{id}")
    public AccountResponse readAccount(@PathVariable UUID id){
        Account account = accountService.getAccount(id);
        return AccountMapper.toResponse(account);
    }

    @GetMapping("/accounts")
    public List<AccountResponse> readAllAccounts(UUID userId){
        return accountService.getAccountsByUserId(userId).stream().map(AccountMapper::toResponse).toList();
    }

    @PostMapping("/accounts")
    public AccountResponse createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        Account newAccount = accountService.createAccount(createAccountRequest.getUserId(), createAccountRequest.getCurrency());
        return AccountMapper.toResponse(newAccount);
    }

    @PutMapping("/accounts/{id}/currency")
    public void updateAccount(@PathVariable UUID id, @RequestParam Currency currency) {
        accountService.changeCurrency(id, currency);
    }

    @DeleteMapping("/accounts/{id}")
    public void deleteAccount(@PathVariable UUID id) {
        accountService.deleteAccount(id);
    }


}
