package com.cvereterra.paymentsservice.command;

import com.cvereterra.espaycore.commands.payments.CreatePaymentSessionCommand;
import com.cvereterra.espaycore.events.payments.PaymentSessionCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Aggregate
public class PaymentSession {
    @AggregateIdentifier
    private UUID sessionId;
    private UUID merchantId;
    private PaymentSessionStatus status;


    public PaymentSession() {
    }

    @CommandHandler
    public PaymentSession(CreatePaymentSessionCommand command) {
        AggregateLifecycle.apply(new PaymentSessionCreatedEvent());
    }

    @EventSourcingHandler
    public void on(PaymentSessionCreatedEvent event) {
        customerId = event.getCustomerId();
        name = event.getName();
        email = event.getEmail();
        address = event.getAddress();
    }
}