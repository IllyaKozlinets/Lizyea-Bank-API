package com.bankapp.controller;

import com.bankapp.model.dto.TransferRequest;
import com.bankapp.model.entity.Transaction;
import com.bankapp.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/api/transfers/")
    @Operation(summary = "Transfer money between accounts")
    public Transaction moneyTransfer(@RequestBody TransferRequest transferRequest) {
        return transactionService.transfer(transferRequest.getFromID(), transferRequest.getToID(), transferRequest.getAmount());
    }
}
