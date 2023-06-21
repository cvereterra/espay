package com.cvereterra.espaycore.commands.adquirer;

import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Value
public class RejectPaymentAdquirementCommand {
    @TargetAggregateIdentifier
    UUID sessionId;
}
