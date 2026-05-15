package com.bankapp.notification;

import com.bankapp.model.dto.TransactionCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventConsumer {

    @RabbitListener(queues = "transaction.queue")
    public void consume (TransactionCreatedEvent event){
        System.out.printf(
                """
                        Transaction received:
                        ID: %s
                        Amount: %s
                        Sender: %s
                        Receiver: %s
                        Status: %s
                        %n""", event.transactionId(),
        event.amount(),
        event.sender(),
        event.receiver(),
        event.status()
);
    }
}
