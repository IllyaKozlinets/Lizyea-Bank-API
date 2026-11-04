package com.bankapp.service;

import com.bankapp.model.entity.Account;
import com.bankapp.model.entity.User;
import com.bankapp.model.entity.enums.Currency;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_ShouldCreateAndReturnAccount() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User mockUser = new User();
        mockUser.setId(userId);

        Currency currency = Currency.USD;

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(accountRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Account createdAccount = accountService.createAccount(userId, currency);

        // Assert
        assertNotNull(createdAccount);
        assertEquals(mockUser, createdAccount.getUser());
        assertEquals(currency, createdAccount.getCurrency());
        assertEquals(BigDecimal.ZERO, createdAccount.getBalance());

        // Перевіряємо, чи викликався збереження репозиторію
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void createAccount_WhenUserNotFound_ShouldThrowException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> accountService.createAccount(userId, Currency.USD));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void getAccount_ShouldReturnAccount() {
        // Arrange
        UUID accountId = UUID.randomUUID();
        Account mockAccount = new Account();
        mockAccount.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // Act
        Account account = accountService.getAccount(accountId);

        // Assert
        assertNotNull(account);
        assertEquals(accountId, account.getId());
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getAccountsByUserId_ShouldReturnListOfAccounts() {
        // Arrange
        UUID userId = UUID.randomUUID();
        List<Account> mockAccounts = List.of(new Account(), new Account());

        when(accountRepository.findAllByUserId(userId)).thenReturn(mockAccounts);

        // Act
        List<Account> accounts = accountService.getAccountsByUserId(userId);

        // Assert
        assertEquals(2, accounts.size());
        verify(accountRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void changeCurrency_ShouldUpdateCurrency() {
        // Arrange
        UUID accountId = UUID.randomUUID();
        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        mockAccount.setCurrency(Currency.USD);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // Act
        accountService.changeCurrency(accountId, Currency.EUR);

        // Assert
        assertEquals(Currency.EUR, mockAccount.getCurrency());
        verify(accountRepository, times(1)).save(mockAccount);
    }

    @Test
    void changeCurrency_WhenSameCurrency_ShouldThrowException() {
        // Arrange
        UUID accountId = UUID.randomUUID();
        Account mockAccount = new Account();
        mockAccount.setId(accountId);
        mockAccount.setCurrency(Currency.USD);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> accountService.changeCurrency(accountId, Currency.USD)
        );

        assertEquals("Same currency", exception.getMessage());
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void deleteAccount_ShouldCallRepositoryDelete() {
        // Arrange
        UUID accountId = UUID.randomUUID();

        // Act
        accountService.deleteAccount(accountId);

        // Assert
        verify(accountRepository, times(1)).deleteById(accountId);
    }
}
