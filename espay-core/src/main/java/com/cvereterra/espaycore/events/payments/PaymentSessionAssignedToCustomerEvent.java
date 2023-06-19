package com.cvereterra.espaycore.events.payments;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Data
public class PaymentSessionAssignedToCustomerEvent {
    @TargetAggregateIdentifier
    private final UUID sessionId;
    private final UUID customerId;
}