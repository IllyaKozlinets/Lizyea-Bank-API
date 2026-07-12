package com.bankapp;

import com.bankapp.model.dto.TransactionCreatedEvent;
import com.bankapp.model.entity.Account;
import com.bankapp.model.entity.Transaction;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.service.AccountService;
import com.bankapp.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static com.bankapp.model.entity.enums.TransactionStatus.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private RabbitTemplate rabbitTemplate;
    @InjectMocks
    private TransactionService transactionService;

    @Test
    void transfer_success_movesBalanceBetweenAccounts() {
        UUID fromID = UUID.randomUUID();
        UUID toID = UUID.randomUUID();

        Account fromAccount = new Account(fromID,null, new BigDecimal("100.000"),null);
        Account toAccount = new Account(toID,null, new BigDecimal("200.000"),null);

        when(accountRepository.findById(fromID)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toID)).thenReturn(Optional.of(toAccount));

        Transaction result = transactionService.transfer(fromID,toID,new BigDecimal("100.000"));

        assertEquals(new BigDecimal("0.000"), fromAccount.getBalance());
        assertEquals(new BigDecimal("300.000"), toAccount.getBalance());

        assertEquals(SUCCESS, result.getStatus());

        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);
        verify(transactionRepository).save(result);


    }

    @Test
    void transfer_throwsWhenAmountIsZeroOrNegative() {
        UUID fromId = UUID.randomUUID();
        UUID toId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transfer(fromId, toId, BigDecimal.ZERO);
        });
    }

    @Test
    void transfer_throwsWhenFromEqualsAccount() {
        UUID fromId = UUID.randomUUID();
        UUID toId = fromId;

        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transfer(fromId,toId,new BigDecimal("100.000"));
        });
    }

    @Test
    void transfer_throwsWhenInsufficientBalance(){
        UUID fromId = UUID.randomUUID();
        UUID toId = UUID.randomUUID();

        Account fromAccount = new Account(fromId,null, new BigDecimal("100.000"),null);
        Account toAccount = new Account(toId,null, new BigDecimal("200.000"),null);

        when(accountRepository.findById(fromId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toId)).thenReturn(Optional.of(toAccount));

        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.transfer(fromId,toId,new BigDecimal("2100.000"));
        });

    }
    @Test
    void transfer_sendsCorrectEventToRabbit() {
        UUID fromId = UUID.randomUUID();
        UUID toId = UUID.randomUUID();
        Account fromAccount = new Account(fromId, null, new BigDecimal("100.000"), null);
        Account toAccount = new Account(toId, null, new BigDecimal("200.000"), null);
        when(accountRepository.findById(fromId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toId)).thenReturn(Optional.of(toAccount));

        Transaction result = transactionService.transfer(fromId, toId, new BigDecimal("30.000"));

        ArgumentCaptor<TransactionCreatedEvent> eventCaptor =
                ArgumentCaptor.forClass(TransactionCreatedEvent.class);

        verify(rabbitTemplate).convertAndSend(eq("transaction.created"), eventCaptor.capture());

        TransactionCreatedEvent capturedEvent = eventCaptor.getValue();
        assertEquals(result.getId(), capturedEvent.transactionId());
        assertEquals(new BigDecimal("30.000"), capturedEvent.amount());
        assertEquals(fromId, capturedEvent.sender());
        assertEquals(toId, capturedEvent.receiver());
        assertEquals(SUCCESS, capturedEvent.status());
    }

    @Test
    void transfer_throwsWhenFromAccountNotFound() {
        UUID fromId = UUID.randomUUID();
        UUID toId = UUID.randomUUID();

        when(accountRepository.findById(fromId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            transactionService.transfer(fromId, toId, new BigDecimal("10.000"));
        });
    }

    @Test
    void transfer_throwsWhenToAccountNotFound() {
        UUID fromId = UUID.randomUUID();
        UUID toId = UUID.randomUUID();
        Account fromAccount = new Account(fromId, null, new BigDecimal("100.000"), null);
        when(accountRepository.findById(fromId)).thenReturn(Optional.of(fromAccount));
        when(accountRepository.findById(toId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> {
            transactionService.transfer(fromId, toId, new BigDecimal("10.000"));
        });
    }
}
