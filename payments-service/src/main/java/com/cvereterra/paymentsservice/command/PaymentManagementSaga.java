package com.cvereterra.paymentsservice.command;

import com.cvereterra.espaycore.commands.adquirer.AdquirePaymentCommand;
import com.cvereterra.espaycore.commands.adquirer.PayMerchantCommand;
import com.cvereterra.espaycore.commands.adquirer.RejectPaymentAdquirementCommand;
import com.cvereterra.espaycore.commands.cardnetwork.AuthorizePaymentSessionCommand;
import com.cvereterra.espaycore.commands.cardnetwork.RejectPaymentSessionCommand;
import com.cvereterra.espaycore.events.adquirer.MerchantPayedEvent;
import com.cvereterra.espaycore.events.adquirer.PaymentAdquiredEvent;
import com.cvereterra.espaycore.events.adquirer.PaymentAdquiringFailedEvent;
import com.cvereterra.espaycore.events.cardnetwork.PaymentSessionAuthorizedEvent;
import com.cvereterra.espaycore.events.cardnetwork.PaymentSessionRejectedEvent;
import com.cvereterra.espaycore.events.payments.PaymentSessionAssignedToCustomerEvent;
import com.cvereterra.espaycore.events.payments.PaymentSessionCreatedEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;


interface CustomerClient {
    @GetExchange("/customers/{customerId}/card-number")
    Mono<String> getCardNumber(@PathVariable String customerId);
}

@Saga
public class PaymentManagementSaga {

    private static final Logger logger = LoggerFactory.getLogger(PaymentManagementSaga.class);
    @Autowired
    private transient CommandGateway commandGateway;

    @Bean
    private static CustomerClient customerClient(WebClient.Builder builder) {
        return HttpServiceProxyFactory.builder(WebClientAdapter.forClient(builder.baseUrl("http://customers:8080").build()))
                .build(
                ).createClient(CustomerClient.class);
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "sessionId")
    public void handle(PaymentSessionCreatedEvent event) {
        logger.info("Saga started");
    }

    @SagaEventHandler(associationProperty = "sessionId")
    public void handle(PaymentSessionAssignedToCustomerEvent event) {
        // Here we will simulate the card network accepting or rejecting the payment
        boolean networkAuthorization = Math.random() >= 0.05;

        customerClient(WebClient.builder()).getCardNumber(event.getCustomerId().toString()).subscribe(cardNumber -> {
            logger.info("Saga handling PaymentSessionAssignedToCustomerEvent, cardNumber  {}", cardNumber);
            if (networkAuthorization) commandGateway.send(new AuthorizePaymentSessionCommand(event.getSessionId()));
            else commandGateway.send(new RejectPaymentSessionCommand(event.getSessionId()));
        });

    }

    @SagaEventHandler(associationProperty = "sessionId")
    public void handle(PaymentSessionAuthorizedEvent event) {
        // Here we will simulate the adquiring result
        boolean adquiredSuccessfully = Math.random() >= 0.05;
        logger.info("Saga handling PaymentSessionAuthorizedEvent, adquiredSuccessfully  {}", adquiredSuccessfully);
        if (adquiredSuccessfully) commandGateway.send(new AdquirePaymentCommand(event.getSessionId()));
        else commandGateway.send(new RejectPaymentAdquirementCommand(event.getSessionId()));
    }

    @SagaEventHandler(associationProperty = "sessionId")
    public void handle(PaymentSessionRejectedEvent event) {
        logger.info("Saga handling PaymentSessionRejectedEvent");
        logger.info("Saga ended");
        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "sessionId")
    public void handle(PaymentAdquiredEvent event) {
        logger.info("Saga handling PaymentAdquiredEvent");
        commandGateway.send(new PayMerchantCommand(event.getSessionId()));
    }

    @SagaEventHandler(associationProperty = "sessionId")
    public void handle(PaymentAdquiringFailedEvent event) {
        logger.info("Saga handling PaymentAdquiringFailedEvent");
        logger.info("Saga ended");
        SagaLifecycle.end();
    }


    @SagaEventHandler(associationProperty = "sessionId")
    public void handle(MerchantPayedEvent event) {
        logger.info("Saga handling MerchantPayedEvent");
        logger.info("Saga ended");
        SagaLifecycle.end();
    }
}
