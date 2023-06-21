package com.cvereterra.paymentsservice.command;

import com.cvereterra.espaycore.commands.adquirer.AdquirePaymentCommand;
import com.cvereterra.espaycore.commands.adquirer.PayMerchantCommand;
import com.cvereterra.espaycore.commands.adquirer.RejectPaymentAdquirementCommand;
import com.cvereterra.espaycore.commands.cardnetwork.AuthorizePaymentSessionCommand;
import com.cvereterra.espaycore.commands.cardnetwork.RejectPaymentSessionCommand;
import com.cvereterra.espaycore.commands.payments.AssignPaymentSessionToCustomerCommand;
import com.cvereterra.espaycore.commands.payments.CreatePaymentSessionCommand;
import com.cvereterra.espaycore.events.adquirer.MerchantPayedEvent;
import com.cvereterra.espaycore.events.adquirer.PaymentAdquiredEvent;
import com.cvereterra.espaycore.events.adquirer.PaymentAdquiringFailedEvent;
import com.cvereterra.espaycore.events.cardnetwork.PaymentSessionAuthorizedEvent;
import com.cvereterra.espaycore.events.cardnetwork.PaymentSessionRejectedEvent;
import com.cvereterra.espaycore.events.payments.PaymentSessionAssignedToCustomerEvent;
import com.cvereterra.espaycore.events.payments.PaymentSessionCreatedEvent;
import com.cvereterra.paymentsservice.api.PaymentController;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Logger logger = LoggerFactory.getLogger(PaymentController.class);


    public PaymentSession() {
    }

    @CommandHandler
    public PaymentSession(CreatePaymentSessionCommand command) {
        logger.info("Handling CreatePaymentSessionCommand {}", command);
        AggregateLifecycle.apply(new PaymentSessionCreatedEvent(
                command.getSessionId(),
                command.getMerchantId(),
                command.getAmount(),
                command.getCurrency()
        ));
    }

    @CommandHandler
    public PaymentSession(AssignPaymentSessionToCustomerCommand command) {
        logger.info("Handling AssignPaymentSessionToCustomerCommand {}", command);
        AggregateLifecycle.apply(new PaymentSessionAssignedToCustomerEvent(
                command.getSessionId(),
                command.getCustomerId()
        ));
    }

    @CommandHandler
    public PaymentSession(AuthorizePaymentSessionCommand command) {
        logger.info("Handling AuthorizePaymentSessionCommand {}", command);
        AggregateLifecycle.apply(new PaymentSessionAuthorizedEvent(
                command.getSessionId()
        ));
    }

    @CommandHandler
    public PaymentSession(RejectPaymentSessionCommand command) {
        logger.info("Handling RejectPaymentSessionCommand {}", command);
        AggregateLifecycle.apply(new PaymentSessionRejectedEvent(
                command.getSessionId()
        ));
    }

    @CommandHandler
    public PaymentSession(AdquirePaymentCommand command) {
        logger.info("Handling AdquirePaymentCommand {}", command);
        AggregateLifecycle.apply(new PaymentAdquiredEvent(
                command.getSessionId()
        ));
    }

    @CommandHandler
    public PaymentSession(RejectPaymentAdquirementCommand command) {
        logger.info("Handling RejectPaymentAdquirementCommand {}", command);
        AggregateLifecycle.apply(new PaymentAdquiringFailedEvent(
                command.getSessionId()
        ));
    }

    @CommandHandler
    public PaymentSession(PayMerchantCommand command) {
        logger.info("Handling PayMerchantCommand {}", command);
        AggregateLifecycle.apply(new MerchantPayedEvent(
                command.getSessionId()
        ));
    }


    @EventSourcingHandler
    public void on(PaymentSessionCreatedEvent event) {
        logger.info("Sourcing PaymentSessionCreatedEvent {}", event);
        sessionId = event.getSessionId();
        merchantId = event.getMerchantId();
        customerId = null;
        currency = event.getCurrency();
        amount = event.getAmount();
        status = PaymentSessionStatus.CREATED;
    }

    @EventSourcingHandler
    public void on(PaymentSessionAssignedToCustomerEvent event) {
        logger.info("Sourcing PaymentSessionAssignedToCustomerEvent {}", event);
        sessionId = event.getSessionId();
        customerId = event.getCustomerId();
        status = PaymentSessionStatus.ASSIGNED_TO_CUSTOMER;
    }

    @EventSourcingHandler
    public void on(PaymentSessionAuthorizedEvent event) {
        logger.info("Sourcing PaymentSessionAuthorizedEvent {}", event);
        sessionId = event.getSessionId();
        status = PaymentSessionStatus.AUTHORIZED_BY_CARD_NETWORK;
    }

    @EventSourcingHandler
    public void on(PaymentSessionRejectedEvent event) {
        logger.info("Sourcing PaymentSessionRejectedEvent {}", event);
        sessionId = event.getSessionId();
        status = PaymentSessionStatus.REJECTED_BY_CARD_NETWORK;
    }

    @EventSourcingHandler
    public void on(PaymentAdquiredEvent event) {
        logger.info("Sourcing PaymentAdquiredEvent {}", event);
        sessionId = event.getSessionId();
        status = PaymentSessionStatus.ADQUIRED;
    }

    @EventSourcingHandler
    public void on(PaymentAdquiringFailedEvent event) {
        logger.info("Sourcing PaymentAdquiringFailedEvent {}", event);
        sessionId = event.getSessionId();
        status = PaymentSessionStatus.ADQUIRING_FAILED;
    }

    @EventSourcingHandler
    public void on(MerchantPayedEvent event) {
        logger.info("Sourcing MerchantPayedEvent {}", event);
        sessionId = event.getSessionId();
        status = PaymentSessionStatus.TRANSFERED_TO_MERCHANT;
    }
}