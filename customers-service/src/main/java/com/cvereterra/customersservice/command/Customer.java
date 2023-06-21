package com.cvereterra.customersservice.command;

import com.cvereterra.espaycore.commands.customer.CreateCustomerCommand;
import com.cvereterra.espaycore.events.customer.CustomerCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

@Aggregate
public class Customer {
    @AggregateIdentifier
    private UUID customerId;
    private String name;
    private String email;
    private String address;

    public Customer() {
    }

    @CommandHandler
    public Customer(CreateCustomerCommand command) {
        AggregateLifecycle.apply(new CustomerCreatedEvent(command.getCustomerId(), command.getName(), command.getEmail(), command.getAddress()));
    }

    @EventSourcingHandler
    public void on(CustomerCreatedEvent event) {
        this.customerId = event.getCustomerId();
        this.name = event.getName();
        this.email = event.getEmail();
        this.address = event.getAddress();
    }
}
