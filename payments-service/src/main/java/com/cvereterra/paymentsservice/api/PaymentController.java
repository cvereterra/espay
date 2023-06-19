package com.cvereterra.paymentsservice.api;

import com.cvereterra.espaycore.commands.customer.CreateCustomerCommand;
import com.cvereterra.espaycore.commands.payments.AssignPaymentSessionToCustomerCommand;
import com.cvereterra.espaycore.queries.payments.FindPaymentSessionByIdQuery;
import com.cvereterra.paymentsservice.query.PaymentSessionView;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("payments")
public class PaymentController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    public PaymentController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @GetMapping("/{sessionId}")
    public CompletableFuture<PaymentSessionView> getCustomerById(@PathVariable("sessionId") String sessionId) {
        return queryGateway.query(new FindPaymentSessionByIdQuery(UUID.fromString(sessionId)), ResponseTypes.instanceOf(PaymentSessionView.class));
    }

    @PostMapping("/")
    public void createCustomer(@RequestBody CreateCustomerCommand command) {
        commandGateway.send(command);
    }

    @PostMapping("/{sessionId}/assign-to-customer")
    public void assignToCustomer(@RequestBody AssignPaymentSessionToCustomerCommand command) {
        commandGateway.send(command);
    }
}
