package com.cvereterra.espaycore.events.cardnetwork;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Value
public class PaymentSessionAuthorizedEvent {
    @TargetAggregateIdentifier
    UUID sessionId;
}