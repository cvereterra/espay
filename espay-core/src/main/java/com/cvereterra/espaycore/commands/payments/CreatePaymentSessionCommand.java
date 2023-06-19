package com.cvereterra.espaycore.commands.payments;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.UUID;

@Value
public class CreatePaymentSessionCommand {
    @TargetAggregateIdentifier
    UUID sessionId;
    UUID merchantId;
    BigDecimal amount;
    String currency;
}