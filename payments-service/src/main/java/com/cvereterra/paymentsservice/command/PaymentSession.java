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

import java.util.UUID;

@Aggregate
public class PaymentSession {
    @AggregateIdentifier
    private UUID sessionId;
    private AuthorizationStatus cardNetworkAuthorizationStatus;
    private AuthorizationStatus adquirerAuthorizationStatus;

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
    public void handle(AssignPaymentSessionToCustomerCommand command) {
        logger.info("Handling AssignPaymentSessionToCustomerCommand {}", command);
        AggregateLifecycle.apply(new PaymentSessionAssignedToCustomerEvent(
                command.getSessionId(),
                command.getCustomerId()
        ));
    }

    @CommandHandler
    public void handle(AuthorizePaymentSessionCommand command) {
        logger.info("Handling AuthorizePaymentSessionCommand {}", command);
        if (cardNetworkAuthorizationStatus != AuthorizationStatus.PENDING) {
            throw new IllegalStateException("Card network is authorizing an already processed payment");
        }
        AggregateLifecycle.apply(new PaymentSessionAuthorizedEvent(
                command.getSessionId()
        ));
    }

    @CommandHandler
    public void handle(RejectPaymentSessionCommand command) {
        logger.info("Handling RejectPaymentSessionCommand {}", command);
        if (cardNetworkAuthorizationStatus != AuthorizationStatus.PENDING) {
            throw new IllegalStateException("Card network is rejecting an already processed payment");
        }
        AggregateLifecycle.apply(new PaymentSessionRejectedEvent(
                command.getSessionId()
        ));
    }

    @CommandHandler
    public void handle(AdquirePaymentCommand command) {
        logger.info("Handling AdquirePaymentCommand {}", command);
        if (cardNetworkAuthorizationStatus != AuthorizationStatus.AUTHORIZED) {
            throw new IllegalStateException("Adquirer trying to process rejected payment by the card network");
        }
        if (adquirerAuthorizationStatus != AuthorizationStatus.PENDING) {
            throw new IllegalStateException("Adquirer is approving an already processed payment");
        }
        AggregateLifecycle.apply(new PaymentAdquiredEvent(
                command.getSessionId()
        ));
    }

    @CommandHandler
    public void handle(RejectPaymentAdquirementCommand command) {
        logger.info("Handling RejectPaymentAdquirementCommand {}", command);
        if (cardNetworkAuthorizationStatus != AuthorizationStatus.AUTHORIZED) {
            throw new IllegalStateException("Adquirer is trying to process rejected payment by the card network");
        }
        if (adquirerAuthorizationStatus != AuthorizationStatus.PENDING) {
            throw new IllegalStateException("Adquirer is rejecting an already processed payment");
        }
        AggregateLifecycle.apply(new PaymentAdquiringFailedEvent(
                command.getSessionId()
        ));
    }

    @CommandHandler
    public void handle(PayMerchantCommand command) {
        logger.info("Handling PayMerchantCommand {}", command);
        AggregateLifecycle.apply(new MerchantPayedEvent(
                command.getSessionId()
        ));
    }


    @EventSourcingHandler
    public void on(PaymentSessionCreatedEvent event) {
        logger.info("Sourcing PaymentSessionCreatedEvent {}", event);
        this.sessionId = event.getSessionId();
        this.cardNetworkAuthorizationStatus = AuthorizationStatus.PENDING;
        this.adquirerAuthorizationStatus = AuthorizationStatus.PENDING;

    }

    @EventSourcingHandler
    public void on(PaymentSessionAssignedToCustomerEvent event) {
        logger.info("Sourcing PaymentSessionAssignedToCustomerEvent {}", event);
    }

    @EventSourcingHandler
    public void on(PaymentSessionAuthorizedEvent event) {
        logger.info("Sourcing PaymentSessionAuthorizedEvent {}", event);
        this.cardNetworkAuthorizationStatus = AuthorizationStatus.AUTHORIZED;
    }

    @EventSourcingHandler
    public void on(PaymentSessionRejectedEvent event) {
        logger.info("Sourcing PaymentSessionRejectedEvent {}", event);
        this.cardNetworkAuthorizationStatus = AuthorizationStatus.REJECTED;
    }

    @EventSourcingHandler
    public void on(PaymentAdquiredEvent event) {
        logger.info("Sourcing PaymentAdquiredEvent {}", event);
        this.adquirerAuthorizationStatus = AuthorizationStatus.AUTHORIZED;
    }

    @EventSourcingHandler
    public void on(PaymentAdquiringFailedEvent event) {
        logger.info("Sourcing PaymentAdquiringFailedEvent {}", event);
        this.adquirerAuthorizationStatus = AuthorizationStatus.REJECTED;
    }

    @EventSourcingHandler
    public void on(MerchantPayedEvent event) {
        logger.info("Sourcing MerchantPayedEvent {}", event);
    }
}