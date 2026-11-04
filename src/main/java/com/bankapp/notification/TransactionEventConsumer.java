package com.bankapp.notification;

import com.bankapp.config.RabbitMQConfig;
import com.bankapp.model.dto.TransactionCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventConsumer {

    @RabbitListener(queues = RabbitMQConfig.TRANSACTION_QUEUE)
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
