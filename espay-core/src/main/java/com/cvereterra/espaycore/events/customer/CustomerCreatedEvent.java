package com.cvereterra.espaycore.events.customer;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Value
public class CustomerCreatedEvent {
    @TargetAggregateIdentifier
    UUID customerId;
    String name;
    String email;
    String address;
    String cardNumber;
}