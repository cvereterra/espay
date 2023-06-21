package com.cvereterra.espaycore.events.adquirer;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Value
public class PaymentAdquiredEvent {
    @TargetAggregateIdentifier
    UUID sessionId;
}