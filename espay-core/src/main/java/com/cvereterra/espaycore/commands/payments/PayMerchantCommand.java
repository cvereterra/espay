package com.cvereterra.espaycore.commands.payments;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Value
public class PayMerchantCommand {
    @TargetAggregateIdentifier
    UUID sessionId;
}