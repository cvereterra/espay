package com.cvereterra.espay.api;

import com.cvereterra.espay.core.commands.CreateCustomerCommand;
import com.cvereterra.espay.core.queries.FindCustomerByIdQuery;
import com.cvereterra.espay.query.CustomerView;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("customers")
public class CustomerController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public CustomerController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping("/")
    public void createCustomer(@RequestBody CreateCustomerCommand command) {
        commandGateway.send(command);
    }

    @GetMapping("/{customerId}")
    public CompletableFuture<CustomerView> getCustomerById(@PathVariable("") String customerId) {
        return queryGateway.query(new FindCustomerByIdQuery(UUID.fromString(customerId)), ResponseTypes.instanceOf(CustomerView.class));
    }

    @GetMapping("/greet")
    public String greet() {
        return "hi";
    }
}
