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

    @Transactional
    public void transfer(UUID fromID, UUID toID, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (fromID.equals(toID)) {
            throw new IllegalArgumentException("From account cannot be the same account as To account");
        }

        var fromAccount = accountRepository.findById(fromID).orElseThrow();
        var toAccount = accountRepository.findById(toID).orElseThrow();

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("From account balance must be greater than amount");
        }

        toAccount.setBalance(toAccount.getBalance().add(amount));
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
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
