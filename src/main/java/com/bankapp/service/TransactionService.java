package com.bankapp.service;

import com.bankapp.model.dto.TransactionCreatedEvent;
import com.bankapp.model.entity.Transaction;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.bankapp.model.entity.enums.TransactionStatus.SUCCESS;

@Service
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final RabbitTemplate rabbitTemplate;

    public TransactionService(AccountRepository accountRepository,  TransactionRepository transactionRepository,  RabbitTemplate rabbitTemplate) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public Transaction transfer(UUID fromID, UUID toID, BigDecimal amount) {
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

        Transaction trans = new Transaction(
                fromID,
                toID,
                amount,
                SUCCESS,
                LocalDateTime.now()
        );

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        transactionRepository.save(trans);

        TransactionCreatedEvent event = new TransactionCreatedEvent(
                trans.getId(),
                trans.getAmount(),
                fromID,
                toID,
                trans.getStatus()
        );

        rabbitTemplate.convertAndSend("transaction.created", event);

        return trans;
    }

}
