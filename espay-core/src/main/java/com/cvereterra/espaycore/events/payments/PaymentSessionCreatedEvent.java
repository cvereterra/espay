package com.cvereterra.espaycore.events.payments;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentSessionCreatedEvent {
    @TargetAggregateIdentifier
    private final UUID sessionId;
    private final UUID merchantId;
    private final BigDecimal amount;
    private final String currency;
}