package com.cvereterra.paymentsservice.query;

import com.cvereterra.espaycore.events.adquirer.MerchantPayedEvent;
import com.cvereterra.espaycore.events.adquirer.PaymentAdquiredEvent;
import com.cvereterra.espaycore.events.adquirer.PaymentAdquiringFailedEvent;
import com.cvereterra.espaycore.events.cardnetwork.PaymentSessionAuthorizedEvent;
import com.cvereterra.espaycore.events.cardnetwork.PaymentSessionRejectedEvent;
import com.cvereterra.espaycore.events.payments.PaymentSessionAssignedToCustomerEvent;
import com.cvereterra.espaycore.events.payments.PaymentSessionCreatedEvent;
import com.cvereterra.espaycore.queries.payments.FindPaymentSessionByIdQuery;
import com.cvereterra.paymentsservice.command.PaymentSessionStatus;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentSessionProjector {
    private final PaymentSessionRepository paymentSessionRepository;
    private Logger logger = LoggerFactory.getLogger(PaymentSessionProjector.class);

    public PaymentSessionProjector(PaymentSessionRepository paymentSessionRepository) {
        this.paymentSessionRepository = paymentSessionRepository;
    }

    @EventHandler
    public void on(PaymentSessionCreatedEvent event) {
        Optional<PaymentSessionView> session = paymentSessionRepository.findById(event.getSessionId());
        if (session.isPresent()) {
            throw new IllegalStateException("Payment session already exists");
        }
        PaymentSessionView paymentSessionView = new PaymentSessionView(
                event.getSessionId(),
                event.getMerchantId(),
                null,
                event.getCurrency(),
                PaymentSessionStatus.CREATED,
                event.getAmount()
        );
        paymentSessionRepository.save(paymentSessionView);
    }

    @EventHandler
    public void on(PaymentSessionAssignedToCustomerEvent event) {
        PaymentSessionView paymentSessionView = this.getExistingPaymentSession(event.getSessionId());
        paymentSessionView.setCustomerId(event.getCustomerId());
        paymentSessionView.setStatus(PaymentSessionStatus.ASSIGNED_TO_CUSTOMER);
        paymentSessionRepository.save(paymentSessionView);
    }


    @EventHandler
    public void on(PaymentSessionAuthorizedEvent event) {
        logger.info("Handling PaymentSessionAuthorizedEvent {}", event);
        PaymentSessionView paymentSessionView = this.getExistingPaymentSession(event.getSessionId());
        paymentSessionView.setStatus(PaymentSessionStatus.AUTHORIZED_BY_CARD_NETWORK);
        paymentSessionRepository.save(paymentSessionView);
    }

    @EventHandler
    public void on(PaymentSessionRejectedEvent event) {
        logger.info("Handling PaymentSessionRejectedEvent {}", event);
        PaymentSessionView paymentSessionView = this.getExistingPaymentSession(event.getSessionId());
        paymentSessionView.setStatus(PaymentSessionStatus.REJECTED_BY_CARD_NETWORK);
        paymentSessionRepository.save(paymentSessionView);
    }

    @EventHandler
    public void on(PaymentAdquiredEvent event) {
        logger.info("Handling PaymentAdquiredEvent {}", event);
        PaymentSessionView paymentSessionView = this.getExistingPaymentSession(event.getSessionId());
        paymentSessionView.setStatus(PaymentSessionStatus.ADQUIRED);
        paymentSessionRepository.save(paymentSessionView);
    }

    @EventHandler
    public void on(PaymentAdquiringFailedEvent event) {
        logger.info("Handling PaymentAdquiringFailedEvent {}", event);
        PaymentSessionView paymentSessionView = this.getExistingPaymentSession(event.getSessionId());
        paymentSessionView.setStatus(PaymentSessionStatus.ADQUIRING_FAILED);
        paymentSessionRepository.save(paymentSessionView);
    }

    @EventHandler
    public void on(MerchantPayedEvent event) {
        logger.info("Handling MerchantPayedEvent {}", event);
        PaymentSessionView paymentSessionView = this.getExistingPaymentSession(event.getSessionId());
        paymentSessionView.setStatus(PaymentSessionStatus.TRANSFERED_TO_MERCHANT);
        paymentSessionRepository.save(paymentSessionView);
    }


    @QueryHandler
    public PaymentSessionView handle(FindPaymentSessionByIdQuery query) {
        return paymentSessionRepository.findById(query.getSessionId()).orElse(null);
    }

    private PaymentSessionView getExistingPaymentSession(UUID sessionId) {
        Optional<PaymentSessionView> session = paymentSessionRepository.findById(sessionId);
        if (session.isEmpty()) {
            throw new IllegalStateException("Payment session does not exist");
        }
        return session.get();
    }
}
