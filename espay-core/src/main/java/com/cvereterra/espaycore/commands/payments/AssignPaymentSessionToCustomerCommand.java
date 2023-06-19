package com.cvereterra.espaycore.commands.payments;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
public class AssignPaymentSessionToCustomerCommand {
    @TargetAggregateIdentifier
    private final UUID sessionId;
    private final UUID customerId;
}