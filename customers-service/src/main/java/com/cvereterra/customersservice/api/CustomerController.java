package com.cvereterra.customersservice.api;


import com.cvereterra.customersservice.query.CustomerView;
import com.cvereterra.espaycore.commands.customer.CreateCustomerCommand;
import com.cvereterra.espaycore.queries.customer.FindCustomerByIdQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;
    private Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    public CustomerController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @GetMapping("/health")
    public Map<String, String> getHealth() {
        return Map.of("status", "Ok");
    }

    @PostMapping("/")
    public void createCustomer(@RequestBody CreateCustomerCommand command) {
        commandGateway.send(command);
    }

    @GetMapping("/{customerId}")
    public CompletableFuture<CustomerView> getCustomerById(@PathVariable("customerId") String customerId) {
        return queryGateway.query(new FindCustomerByIdQuery(UUID.fromString(customerId)), ResponseTypes.instanceOf(CustomerView.class));
    }

    @GetMapping("/{customerId}/card-number")
    public CompletableFuture<String> getCustomerCardNumberById(@PathVariable("customerId") String customerId) {
        return queryGateway.query(new FindCustomerByIdQuery(UUID.fromString(customerId)), ResponseTypes.instanceOf(CustomerView.class)).thenApplyAsync(CustomerView::getCardNumber);
    }

}
