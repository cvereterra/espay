package com.cvereterra.espaycore.events.payments;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class PaymentSessionCreatedEvent {
    @TargetAggregateIdentifier
    UUID sessionId;
    UUID merchantId;
    BigDecimal amount;
    String currency;
}