package com.cvereterra.espaycore.commands.payments;

import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreatePaymentSessionCommand {
    @TargetAggregateIdentifier
    private final UUID sessionId;
    private final UUID merchantId;
    private final String name;
    private final BigDecimal amount;
    private final String currency;
}