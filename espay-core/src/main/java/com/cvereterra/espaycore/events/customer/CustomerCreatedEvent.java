package com.cvereterra.espaycore.events.customer;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
public class CustomerCreatedEvent {
    @TargetAggregateIdentifier
    private final UUID customerId;
    private final String name;
    private final String email;
    private final String address;
}