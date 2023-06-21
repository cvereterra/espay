package com.cvereterra.espaycore.commands.customer;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Value
public class CreateCustomerCommand {
    @TargetAggregateIdentifier
    UUID customerId;
    String name;
    String email;
    String address;
    String cardNumber;
}