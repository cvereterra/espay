package com.cvereterra.paymentsservice.query;

import com.cvereterra.espaycore.events.payments.PaymentSessionAssignedToCustomerEvent;
import com.cvereterra.espaycore.events.payments.PaymentSessionCreatedEvent;
import com.cvereterra.espaycore.queries.payments.FindPaymentSessionByIdQuery;
import com.cvereterra.paymentsservice.command.PaymentSessionStatus;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentSessionProjector {
    private final PaymentSessionRepository paymentSessionRepository;

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
        Optional<PaymentSessionView> session = paymentSessionRepository.findById(event.getSessionId());
        if (session.isEmpty()) {
            throw new IllegalStateException("Payment session does not exist");
        }
        PaymentSessionView paymentSessionView = session.get();
        paymentSessionView.setCustomerId(event.getCustomerId());
        paymentSessionView.setStatus(PaymentSessionStatus.ASSIGNED_TO_CUSTOMER);
        paymentSessionRepository.save(paymentSessionView);
    }


    @QueryHandler
    public PaymentSessionView handle(FindPaymentSessionByIdQuery query) {
        return paymentSessionRepository.findById(query.getSessionId()).orElse(null);
    }
}
