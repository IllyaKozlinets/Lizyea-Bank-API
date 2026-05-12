package com.bankapp.service;

import com.bankapp.model.entity.Account;
import com.bankapp.model.entity.enums.Currency;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account createAccount(UUID userId, Currency currency) {
        Account account = new Account();
        var user = userRepository.findById(userId).orElseThrow();
        account.setUser(user);
        account.setCurrency(currency);
        account.setBalance(BigDecimal.ZERO);
        accountRepository.save(account);
        return account;
    }

    public Account getAccount(UUID id){
        return accountRepository.findById(id).orElseThrow();
    }

    public List<Account> getAccountsByUserId(UUID id){
        return accountRepository.findById(id).stream().toList();
    }

    public void changeCurrency(UUID id, Currency currency){
        Account account = accountRepository.findById(id).orElseThrow();
        if (currency.equals(account.getCurrency())) {
            throw new IllegalArgumentException("Same currency");
        }
        account.setCurrency(currency);
        accountRepository.save(account);
    }

    public void deleteAccount(UUID id){
        accountRepository.deleteById(id);
    }
}
