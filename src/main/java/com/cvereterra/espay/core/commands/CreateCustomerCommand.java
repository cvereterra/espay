package com.cvereterra.espay.core.commands;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
public class CreateCustomerCommand{
    @TargetAggregateIdentifier
    private final UUID customerId;
    private final String name;
    private final String email;
    private final String address;
}