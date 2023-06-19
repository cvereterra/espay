package com.cvereterra.paymentsservice.command;

import com.cvereterra.espaycore.commands.payments.AssignPaymentSessionToCustomerCommand;
import com.cvereterra.espaycore.commands.payments.CreatePaymentSessionCommand;
import com.cvereterra.espaycore.events.payments.PaymentSessionAssignedToCustomerEvent;
import com.cvereterra.espaycore.events.payments.PaymentSessionCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;
import java.util.UUID;

@Aggregate
public class PaymentSession {
    @AggregateIdentifier
    private UUID sessionId;
    private UUID merchantId;
    private String currency;
    private BigDecimal amount;
    private UUID customerId;
    private PaymentSessionStatus status;


    public PaymentSession() {
    }

    @CommandHandler
    public PaymentSession(CreatePaymentSessionCommand command) {
        AggregateLifecycle.apply(new PaymentSessionCreatedEvent(
                command.getSessionId(),
                command.getMerchantId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @CommandHandler
    public PaymentSession(AssignPaymentSessionToCustomerCommand command) {
        AggregateLifecycle.apply(new PaymentSessionAssignedToCustomerEvent(
                command.getSessionId(),
                command.getCustomerId()
        ));
    }

    @EventSourcingHandler
    public void on(PaymentSessionCreatedEvent event) {
        sessionId = event.getSessionId();
        merchantId = event.getMerchantId();
        customerId = null;
        currency = event.getCurrency();
        amount = event.getAmount();
        status = PaymentSessionStatus.CREATED;
    }

    @EventSourcingHandler
    public void on(PaymentSessionAssignedToCustomerEvent event) {
        sessionId = event.getSessionId();
        customerId = event.getCustomerId();
        status = PaymentSessionStatus.ASSIGNED_TO_CUSTOMER;
    }
}